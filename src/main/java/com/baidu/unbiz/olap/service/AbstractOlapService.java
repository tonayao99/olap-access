package com.baidu.unbiz.olap.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.annotation.OlapTablePlus;
import com.baidu.unbiz.olap.cache.OlapCacheHandler;
import com.baidu.unbiz.olap.common.ListPager;
import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;
import com.baidu.unbiz.olap.driver.bo.OlapRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.factory.OlapDriverFactory;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.obj.DateTriple;
import com.baidu.unbiz.olap.obj.Pair;
import com.baidu.unbiz.olap.utils.ArrayUtils;
import com.baidu.unbiz.olap.utils.DateUtils;
import com.baidu.unbiz.olap.utils.OlapUtils;
import com.baidu.unbiz.olap.utils.ReportUtils;

/**
 * 基本统计数据查询类，主要用于查询olap
 * 
 * @author wangchongjie
 * @fileName AbstractOlapService.java
 * @dateTime 2015-7-15 下午7:30:30
 */
public abstract class AbstractOlapService implements OlapService {

    protected static final Logger LOG = AopLogFactory.getLogger(AbstractOlapService.class);

    @Resource
    protected OlapCacheWrapper olapCacheWrapper;
    
    @Resource
    protected OlapDriverFactory olapDriverFactory;

    // need inject
    protected OlapEngineDriver olapEngineDriver;
    
    // default config
    @Resource(name = "defaultOlapInstanceConfig")
    protected OlapInstanceConfig olapConfig;

    protected OlapInstanceConfig config() {
        return olapConfig;
    }
    
    @PostConstruct
    protected void preInit() {
        olapEngineDriver = olapDriverFactory.newOlapEngineDriver(config());
    }
    
    /**
     * 子类可重写实现定制化，建议配置优先
     */
    protected DateTriple getStartDate() {
        //OlapEngine表起始可查时间三元组
        return new DateTriple(config().startYear(), config().startMonth(), config().startDay());
    }

    /**
     * 插件化异步缓存handler，子类可重写
     */
    protected OlapCacheHandler getOlapCacheHandler() {
        return null;
    }

    /**
     * 缓存包装类实现，子类可重写
     */
    protected OlapCacheWrapper getOlapCacheWrapper() {
        return this.olapCacheWrapper;
    }

    /**
     * 前后端timeUnit字段映射，后端常量见OlapConstants类
     */
    protected <T> Integer convertOlapTimeUnit(T frontTimeUnit) {
        return null;
    }

    /**
     * 前后端orderByColumn列映射,后端常量对应olap表字段名
     */
    protected String convertOrderByColumn(String frontColumn) {
        return null;
    }

    /**
     * 强制不分页排序
     */
    protected boolean disablePager() {
        return false;
    }

    /**
     * 完成初始化工作，子类重写时建议调用super方法
     */
    protected <T extends ItemAble> void init(ReportRequest<T> rr) {
        // timeUnit convert
        Integer frontTimeUnit = rr.getTimeUnit();
        Integer olapTimeUnit = this.convertOlapTimeUnit(frontTimeUnit);
        rr.setTimeUnit(olapTimeUnit == null ? frontTimeUnit : olapTimeUnit);

        // orderBy column convert
        List<Pair<String, SortOrder>> orderPairs = rr.getOrderPairs();
        if (CollectionUtils.isNotEmpty(orderPairs)) {
            for (Pair<String, SortOrder> pair : orderPairs) {
                String frontColumn = pair.field1;
                String column = this.convertOrderByColumn(frontColumn);
                pair.field1 = (column == null ? frontColumn : column);
            }
        }

        // disable pager or not
        if (disablePager()) {
            rr.setPage(-1);
        }
    }

    /**
     * OlapEngine表数据查询封装
     */
    @Override
    public <T extends ItemAble> List<T> getStorageData(ReportRequest<T> rr) {
        this.init(rr);

        OlapCacheWrapper olapCacheWrapper = this.getOlapCacheWrapper();
        List<T> result = olapCacheWrapper.queryFromCache(rr, this.getOlapCacheHandler());
        if (null != result) {
            return result;
        }
        result = new ArrayList<T>();

        List<Number> userIds = rr.getUserIds();
        List<String> filters = rr.getFilters();
        Class<? extends ItemAble> clazz = rr.getItemClazz();

        OlapRequest<T> request = new OlapRequest<T>();
        OlapTable table = this.findOlapTableByClinetSpecify(rr);

        this.fillRequestByParameters(request, rr);
        this.fillRequestByAnnotation(request, table, filters, clazz);
        this.forceQueryTableByClientSpecify(request, rr);
        this.forceUseKeyValByClientSpecify(request, rr);

        Map<String, List<Number>> userIdSubGroups = this.dispatchUserIds(userIds);
        for (List<Number> subUserIds : userIdSubGroups.values()) {
            Number[] userIdArray = new Number[subUserIds.size()];
            subUserIds.toArray(userIdArray);
            request.setUserIds(userIdArray); // userid

            result.addAll(olapEngineDriver.getStorageData(request));
        }

        if (rr.isMultiTable()) {
            result = this.mergeMultiTableResult(rr, result, userIdSubGroups);
            for (T t : result) {
                t.afterMultiTableQueryAssemble();
            }
        }

        olapCacheWrapper.putIntoCache(rr, result, this.getOlapCacheHandler());
        return result;
    }

    /**
     * OlapEnggine表记录数统计
     */
    @Override
    public <T extends ItemAble> int countStorageData(ReportRequest<T> rr) {
        this.init(rr);

        OlapCacheWrapper olapCacheWrapper = this.getOlapCacheWrapper();
        Integer result = olapCacheWrapper.countFromCache(rr, this.getOlapCacheHandler());
        if (null != result) {
            return result;
        }

        List<Number> userIds = rr.getUserIds();
        List<String> filters = rr.getFilters();
        Class<? extends ItemAble> clazz = rr.getItemClazz();

        OlapRequest<T> request = new OlapRequest<T>();
        OlapTable table = this.findOlapTableByClinetSpecify(rr);

        this.fillRequestByParameters(request, rr);
        this.fillRequestByAnnotation(request, table, filters, clazz);
        this.forceQueryTableByClientSpecify(request, rr);
        this.forceUseKeyValByClientSpecify(request, rr);

        Map<String, List<Number>> userIdSubGroups = this.dispatchUserIds(userIds);

        result = 0;
        for (List<Number> subUserIds : userIdSubGroups.values()) {
            Number[] userIdArray = new Number[subUserIds.size()];
            subUserIds.toArray(userIdArray);
            request.setUserIds(userIdArray); // userid

            int cnt = olapEngineDriver.countStorageData(request);
            result += cnt;
        }

        olapCacheWrapper.putCountIntoCache(rr, result, this.getOlapCacheHandler());
        return result;
    }

    /**
     * OlapEngine表数据批量查询封装
     */
    @Override
    public <T extends ItemAble> List<T> getBatchStorageData(ReportRequest<T> rr, int batchSize) {
        final List<T> result = new ArrayList<T>();
        ResultHandler<T> handler = new ResultHandler<T>() {
            @Override
            public void process(List<T> itemList) {
                if (CollectionUtils.isNotEmpty(itemList)) {
                    result.addAll(itemList);
                }
            }

            @Override
            public void cleanup() {
            }
        };
        this.getBatchStorageData(rr, batchSize, handler);
        return result;
    }
    
    /**
     * OlapEngine表数据批量查询封装,支持自定义handler
     */
    public <T extends ItemAble> void getBatchStorageData(ReportRequest<T> rr, int batchSize, ResultHandler<T> handler) {
        this.init(rr);

        List<String> filters = rr.getFilters();
        Class<? extends ItemAble> clazz = rr.getItemClazz();

        OlapRequest<T> request = new OlapRequest<T>();
        OlapTable table = this.findOlapTableByClinetSpecify(rr);

        this.fillRequestByParameters(request, rr);
        this.fillRequestByAnnotation(request, table, filters, clazz);
        this.forceQueryTableByClientSpecify(request, rr);
        this.forceUseKeyValByClientSpecify(request, rr);

        List<Number> userIds = rr.getUserIds();
        if(CollectionUtils.isEmpty(userIds)) {
            userIds = this.getAllUniqUserIds(request, rr.getIndexTable());
        }
        Map<String, List<Number>> userIdSubGroups = this.dispatchUserIds(userIds);
        int step = batchSize <= 0 ? OlapConstants.BATCH_QUERY_SIZE : batchSize;
        userIds = null; // let gc

        List<T> result = new ArrayList<T>();
        for (List<Number> subUserIds : userIdSubGroups.values()) {
            ListPager<Number> pager = new ListPager<Number>(subUserIds, step);
            while (pager.hasNext()) {
                List<Number> partUserIds = pager.nextPage();
                Number[] userIdArray = new Number[partUserIds.size()];
                partUserIds.toArray(userIdArray);

                request.setUserIds(userIdArray); // userid
                result = olapEngineDriver.getStorageData(request);
                handler.process(result);
            }
        }
        handler.cleanup();
    }

    public void getBatchStorageData(OlapRequest<?> request, int batchSize, ResultHandler<String> handler) {
        List<Number> userIds = this.getAllUniqUserIds(request, null);
        Map<String, List<Number>> userIdSubGroups = this.dispatchUserIds(userIds);
        userIds = null; // let gc
        int step = batchSize <= 0 ? OlapConstants.BATCH_QUERY_SIZE : batchSize;

        List<String> result = new ArrayList<String>();
        for (List<Number> subUserIds : userIdSubGroups.values()) {
            ListPager<Number> pager = new ListPager<Number>(subUserIds, step);
            while (pager.hasNext()) {
                List<Number> partUserIds = pager.nextPage();
                Number[] userIdArray = new Number[partUserIds.size()];
                partUserIds.toArray(userIdArray);

                request.setUserIds(userIdArray); // userid
                result = olapEngineDriver.getStrStorageData(request);
                handler.process(result);
            }
        }
        handler.cleanup();
    }

    private <T extends ItemAble> List<Number> getAllUniqUserIds(OlapRequest<T> request, String indexTable) {
        Number[] uids = request.getUserIds();
        List<Number> userIds;
        if (ArrayUtils.isArrayEmpty(uids)) {
            userIds = olapEngineDriver.getAllUniqUserIds(request, null);
        } else {
            userIds = ArrayUtils.arrayToList(uids);
        }
        return userIds;
    }

    private <T extends ItemAble> void forceQueryTableByClientSpecify(OlapRequest<T> request, ReportRequest<T> rr) {
        if (StringUtils.isNotEmpty(rr.getForceTable())) {
            request.setTable(rr.getForceTable());
        }
    }

    private <T extends ItemAble> void forceUseKeyValByClientSpecify(OlapRequest<T> request,
            ReportRequest<T> rr) {
        List<String> forceKeyVal = rr.getForceKeyVal();
        if (CollectionUtils.isNotEmpty(forceKeyVal)) {
            request.setGroupByCols(forceKeyVal.toArray(new String[forceKeyVal.size()]));
        }
        if (ArrayUtils.isArrayEmpty(request.getGroupByCols())) {
            return;
        }
        if (CollectionUtils.isEmpty(rr.getForceUnUseKeyVal())) {
            return;
        }
        List<String> fixedGroupByCols = new ArrayList<String>(request.getGroupByCols().length);
        for (String groupByCol : request.getGroupByCols()) {
            if (!rr.getForceUnUseKeyVal().contains(groupByCol)) {
                fixedGroupByCols.add(groupByCol);
            }
        }
        request.setGroupByCols(fixedGroupByCols.toArray(new String[fixedGroupByCols.size()]));
    }
    
    private <T extends ItemAble> OlapTable findOlapTableByClinetSpecify(ReportRequest<T> rr) {
        Class<? extends ItemAble> clazz = rr.getItemClazz();
        OlapTable table = clazz.getAnnotation(OlapTable.class);

        String forceTable = rr.getForceTable();
        if (StringUtils.isEmpty(forceTable)) {
            return table;
        }

        String regex = "(.*,)?" + forceTable + "(,.*)?";
        if (table.name().matches(regex)) {
            return table;
        }

        OlapTablePlus tablePlus = clazz.getAnnotation(OlapTablePlus.class);
        if (tablePlus == null) {
            return table;
        }
        for (OlapTable t : tablePlus.value()) {
            if (t.name().matches(regex)) {
                table = t;
                break;
            }
        }
        return table;
    }

    /**
     * 报表Item查询多张olap表数据时调用此接口 工作步骤：
     * 1、查询OlapTable配置的主表数据
     * 2、查询OlapTablePlus配置的次表数据 
     * 3、将次表数据merge到主表中
     * 4、若OlapTablePlus中配置多张次表，则重复2-3 5、返回merge后的Item数据
     * 
     */
    private <T extends ItemAble> List<T> mergeMultiTableResult(ReportRequest<T> rr, List<T> mainRes,
            Map<String, List<Number>> userIdSubGroups) {

        int timeUnit = rr.getTimeUnit();
        List<String> filters = rr.getFilters();

        Class<? extends ItemAble> clazz = rr.getItemClazz();
        OlapTablePlus tablePlus = clazz.getAnnotation(OlapTablePlus.class);
        if (null == tablePlus) {
            return mainRes;
        }

        OlapTable mainTable = clazz.getAnnotation(OlapTable.class);
        OlapTable[] tables = tablePlus.value();

        for (OlapTable table : tables) {

            OlapRequest<T> request = new OlapRequest<T>();
            this.fillRequestByParameters(request, rr);
            this.fillRequestByAnnotation(request, table, filters, clazz);

            List<T> subRes = new ArrayList<T>();
            for (List<Number> subUserIds : userIdSubGroups.values()) {
                Integer[] userIdArray = new Integer[subUserIds.size()];
                subUserIds.toArray(userIdArray);
                request.setUserIds(userIdArray); // userid
                request.setMultiUser(this.isMultiUserQuery(rr));
                subRes.addAll(olapEngineDriver.getStorageData(request));
            }

            boolean isMultiUser = this.isMultiUserQuery(rr);
            this.mergeMultiOlapTableItems(mainRes, subRes, mainTable, table, timeUnit, clazz, isMultiUser, true);
        }

        return mainRes;
    }

    private <T extends ItemAble> boolean isMultiUserQuery(ReportRequest<T> rr) {
        if (rr.getUserIds().size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    private <T extends ItemAble> boolean isRangeUserQuery(ReportRequest<T> rr) {
        if (rr.getUserIds().size() < 1) {
            return true;
        } else {
            return false;
        }
    }

    private <T extends ItemAble> void fillRequestByParameters(OlapRequest<T> request, ReportRequest<T> rr) {

        // 日期校验
        Date[] ensured = ensureDate(rr.getFrom(), rr.getTo(), getStartDate());
        Date from = ensured[0];
        Date to = ensured[1];

        int timeUnit = rr.getTimeUnit();
        int page = rr.getPage();
        int pageSize = rr.getPageSize();
        List<Pair<String, SortOrder>> pairs = rr.getOrderPairs();

        Class<? extends ItemAble> clazz = rr.getItemClazz();
        request.setMultiUser(this.isMultiUserQuery(rr)); // 是否为多用户查询
        request.setRangeUser(this.isRangeUserQuery(rr)); // 是否为范围用户查询

        this.fillRequestByParameters(request, clazz, from, to, pairs, timeUnit, page, pageSize);
    }

    private <T extends ItemAble> void fillRequestByParameters(OlapRequest<T> request, Class<? extends ItemAble> clazz,
            Date from, Date to, List<Pair<String, SortOrder>> orderPairs, int timeUnit, int page, int pageSize) {

        request.setItemClazz(clazz); // 返回类型
        request.setFrom(from); // 查询起始时间
        request.setTo(to); // 查询终止时间
        request.setOrderPairs(orderPairs); // 排序列&排序向
        request.setTimeUnit(timeUnit); // 时间粒度
        request.setPage(page); // 第几页
        request.setPageSize(pageSize); // 每页大小
    }

    private <T extends ItemAble> void fillRequestByAnnotation(OlapRequest<T> request, OlapTable table,
            List<String> filters, Class<? extends ItemAble> clazz) {
        // 表名
        if (StringUtils.isNotEmpty(table.name())) {
            request.setTable(table.name());
        }
        // key列
        if (ArrayUtils.isArrayNotEmpty(table.keyVal())) {
            request.setGroupByCols(table.keyVal());
        }
        // value列
        if (ArrayUtils.isArrayNotEmpty(table.basicVal())) {
            request.setBasicValueCols(table.basicVal());
        }
        // 扩展列名
        if (ArrayUtils.isArrayNotEmpty(table.extCol())) {
            request.setExtensionCols(table.extCol());
        }
        // 扩展列名
        if (ArrayUtils.isArrayNotEmpty(table.extExpr())) {
            request.setExtensionColComps(table.extExpr());
        }
        // 过滤条件
        List<String> filterList = new ArrayList<String>();
        if (ArrayUtils.isArrayNotEmpty(table.filter())) {
            filterList.addAll(Arrays.asList(table.filter()));
        }
        if (CollectionUtils.isNotEmpty(filters)) {
            filterList.addAll(filters);
        }
        request.setFilterConditions(CollectionUtils.isEmpty(filterList) ? null : filterList
                .toArray(new String[filterList.size()]));

        if (ArrayUtils.isArrayNotEmpty(table.aliasCol())) {
            request.setAliasCols(table.aliasCol());
        }
    }

    private Map<String, List<Number>> dispatchUserIds(List<Number> userIds) {
        Map<String, List<Number>> userIdSubGroups = new HashMap<String, List<Number>>();
        if (CollectionUtils.isEmpty(userIds)) {
            Set<Number> userIdDelegate = olapEngineDriver.getSchemaLoader().getUserIdDelegate();
            this.doDispatchUserIds(new ArrayList<Number>(userIdDelegate), userIdSubGroups);
        } else {
            this.doDispatchUserIds(userIds, userIdSubGroups);
        }
        return userIdSubGroups;
    }

    private void doDispatchUserIds(List<Number> userIds, Map<String, List<Number>> userIdSubGroups) {
        for (Number userId : userIds) {
            String userIdFlag = this.olapEngineDriver.getDBShardingRule().calculateDatabaseNo(userId);
            List<Number> userIdSubGroup = userIdSubGroups.get(userIdFlag);
            if (userIdSubGroup == null) {
                userIdSubGroup = new ArrayList<Number>(userIdSubGroups.size());
                userIdSubGroups.put(userIdFlag, userIdSubGroup);
            }
            userIdSubGroup.add(userId);
        }
    }

    // 主表与次表的相同key字段做联合key进行merge操作
    private <T extends ItemAble> List<T> mergeMultiOlapTableItems(List<T> finalRes, List<T> subRes,
            OlapTable mainTable, OlapTable subTable, int timeUnit, Class<? extends ItemAble> clazz,
            boolean isMultiUser, boolean appendMisMatch) {

        // 获取联合key字段
        List<String> mergeKeys = new ArrayList<String>();
        if (timeUnit != OlapConstants.TU_NONE) {
            mergeKeys.add(OlapConstants.COLUMN.TIME);
        }
        if (isMultiUser) {
            mergeKeys.add(config().userId());
        }

        List<String> keysInAnnotation = OlapUtils.getOlapMergeKeyColumns(clazz);
        if (CollectionUtils.isEmpty(keysInAnnotation)) {
            String[] mainKeys = mainTable.keyVal();
            String[] subKeys = subTable.keyVal();
            if (null != mainKeys && null != subKeys) {
                @SuppressWarnings("unchecked")
                List<String> intersec =
                        (List<String>) CollectionUtils.intersection(Arrays.asList(mainKeys), Arrays.asList(subKeys));
                mergeKeys.addAll(intersec);
            }
        } else {
            mergeKeys.addAll(keysInAnnotation);
        }
        // 去重
        Set<String> mergeKeySet = new HashSet<String>();
        mergeKeySet.addAll(mergeKeys);

        // 获取次表的value字段
        String[] basicVals = subTable.basicVal();
        String[] aliasCols = subTable.aliasCol();
        Set<String> mergeVals = new HashSet<String>(Arrays.asList(basicVals));
        if (ArrayUtils.isArrayNotEmpty(aliasCols)) {
            Map<String, String> aliasColDict = new HashMap<String, String>();
            OlapUtils.fillAliasColumnDict(aliasCols, clazz, aliasColDict, null);
            String alias;
            for (String val : basicVals) {
                alias = aliasColDict.get(val);
                if (alias != null) {
                    mergeVals.remove(val);
                    mergeVals.add(aliasColDict.get(val));
                }
            }
        }

        ReportUtils.joinItemListKeepOrder(finalRes, subRes, mergeKeySet, mergeVals, clazz, SortOrder.ASC, true, true);
        return finalRes;
    }

    /**
     * 保证Date[0]<Date[1]，并且Date[0]为0时，Date[1]为23时
     */
    protected Date[] ensureDate(Date from, Date to, DateTriple dt) {
        int startYear = this.config().startYear();
        int startMonth = this.config().startMonth();
        int startDay = this.config().startDay();

        if (null != dt && dt.getDateYYYY() > 0 && dt.getDateMM() > 0 && dt.getDateDD() > 0) {
            startYear = dt.getDateYYYY();
            startMonth = dt.getDateMM();
            startDay = dt.getDateDD();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth, startDay);
        return DateUtils.ensureDate(from, to, calendar.getTime());
    }

//    @Deprecated
//    public List<? extends olapAble> getStorageData(Class<? extends olapAble> clazz, int userId, Date from, Date to,
//            String column, int order, int timeUnit, int page, int pageSize) {
//        return this.getStorageData(clazz, null, userId, from, to, column, order, timeUnit, page, pageSize);
//    }
//
//    @Deprecated
//    public List<? extends olapAble> getStorageData(Class<? extends olapAble> clazz, List<String> filters, int userId,
//            Date from, Date to, String column, int order, int timeUnit, int page, int pageSize) {
//        return this.getStorageData(clazz, filters, Arrays.asList(new Integer[] { userId }), from, to, column, order,
//                timeUnit, page, pageSize);
//    }
//
//    @Deprecated
//    public List<? extends olapAble> getStorageData(Class<? extends olapAble> clazz, List<Integer> userIds, Date from,
//            Date to, String column, int order, int timeUnit, int page, int pageSize) {
//        return this.getStorageData(clazz, null, userIds, from, to, column, order, timeUnit, page, pageSize);
//    }
//
//    /**
//     * OlapEngine单表数据查询封装
//     */
//    @SuppressWarnings("unchecked")
//    @Deprecated
//    public List<? extends olapAble> getStorageData(Class<? extends olapAble> clazz, List<String> filters,
//            List<Integer> userIds, Date from, Date to, String column, int order, int timeUnit, int page, int pageSize) {
//
//        // this.init();
//        @SuppressWarnings("rawtypes")
//        OlapRequest request = new OlapRequest();
//
//        OlapTable table = clazz.getAnnotation(OlapTable.class);
//        this.fillRequestByAnnotation(request, table, filters, clazz);
//
//        // 日期校验
//        Date[] ensured = ensureDate(from, to, getStartDate());
//        from = ensured[0];
//        to = ensured[1];
//
//        Integer[] userIdArray = new Integer[userIds.size()];
//        userIds.toArray(userIdArray);
//
//        List<Pair<String, SortOrder>> orderPairs =
//                Pair.wrapList(new Pair<String, SortOrder>(column, SortOrder.val(order)));
//        this.fillRequestByParameters(request, clazz, from, to, orderPairs, timeUnit, page, pageSize);
//
//        boolean isMultiUser = userIds.size() > 1 ? true : false;
//        request.setMultiUser(isMultiUser);
//        request.setUserIds(userIdArray); // userid
//
//        return olapEngineDriver.getStorageData(request);
//    }
}
