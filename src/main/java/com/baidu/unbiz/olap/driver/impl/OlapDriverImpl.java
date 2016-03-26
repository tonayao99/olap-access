package com.baidu.unbiz.olap.driver.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.baidu.unbiz.olap.common.ThreadContext;
import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;
import com.baidu.unbiz.olap.driver.bo.OlapContext;
import com.baidu.unbiz.olap.driver.bo.OlapCountContext;
import com.baidu.unbiz.olap.driver.bo.OlapQueryContext;
import com.baidu.unbiz.olap.driver.bo.OlapRequest;
import com.baidu.unbiz.olap.exception.OlapException;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.mapper.GeneralRowMapper;
import com.baidu.unbiz.olap.mapper.OlapMapTypeRowMapper;
import com.baidu.unbiz.olap.mapper.OlapOneColumnRowMapper;
import com.baidu.unbiz.olap.mapper.OlapStatRowMapper;
import com.baidu.unbiz.olap.mapper.OlapStringTypeRowMapper;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.obj.Pair;
import com.baidu.unbiz.olap.utils.ArrayUtils;
import com.baidu.unbiz.olap.utils.DateUtils;
import com.baidu.unbiz.olap.utils.StringUtils;

/**
 * OLAP数据查询DAO层Driver封装
 * 
 * @author wangchongjie
 * @fileName OlapStatDaoImpl.java
 * @dateTime 2013-12-4 下午1:18:55
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OlapDriverImpl extends AbstractOlapDriver implements OlapEngineDriver {

    protected static final Logger LOG = AopLogFactory.getLogger(OlapDriverImpl.class);

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T extends ItemAble> List<T> getStorageData(OlapRequest<T> request) {
        this.lazyInit();
        if (!this.validateParameter(request)) {
            String errMsg = "OlapRequest format is wrong: " + request.toString();
            throw new OlapException(errMsg);
        }
        this.putDbShardKeyToThreadContext(request);   // db路由
        String sql = this.buildQueryOlapSql(request); // 构建sql
        LOG.info("[OLAP SQL] " + sql);
        // 从olap中取数据
        List<T> result = this.findBySQL(sql, Collections.emptyList(),
                        new OlapStatRowMapper(request.getItemClazz(), request.getTimeUnit()));

        this.filterNull(result); // 过滤空行
        return result;
    }

    @Override
    public List<String> getStrStorageData(OlapRequest<?> request) {
        this.lazyInit();
        if (null == request || ArrayUtils.isArrayEmpty(request.getUserIds())) {
            String errMsg = "OlapRequest format is wrong: " + request.toString();
            throw new OlapException(errMsg);
        }

        this.putDbShardKeyToThreadContext(request);   // db路由
        String sql = this.buildQueryOlapSql(request); // 构建sql
        LOG.info("[OLAP SQL] " + sql);
        @SuppressWarnings("unchecked")
        List<String> result = this.findBySQL(sql, Collections.emptyList(), new OlapStringTypeRowMapper());
        return result;
    }

    @Override
    public <T extends ItemAble> int countStorageData(OlapRequest<T> request) {
        this.lazyInit();
        if (!this.validateParameter(request)) {
            String errMsg = "OlapRequest format is wrong: " + request.toString();
            throw new OlapException(errMsg);
        }
        this.putDbShardKeyToThreadContext(request);   // db路由
        String sql = this.buildCountOlapSql(request); // 构建sql
        LOG.info("[OLAP SQL] " + sql);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> olapResult =
                this.findByGeneralSQL(sql, Collections.emptyList(), new GeneralRowMapper());
        Object result = this.extractFirstValue(olapResult);

        return result == null ? 0 : Integer.parseInt(result.toString());
    }

    // 构建Olap查询SQL
    private <T extends ItemAble> String buildQueryOlapSql(OlapRequest<T> request) {
        StringBuilder sql = new StringBuilder("SELECT ");
        OlapContext context = new OlapQueryContext(request);

        this.appendQueryCols(sql, request, context);         // 查询列
        this.appendExtensionCols(sql, request, context);     // 扩展列
        this.appendTimeUnitCol(sql, request);                // 时间粒度
        this.appendTableName(sql, request);                  // 表名
        this.appendWhereConditions(sql, request);            // 查询条件
        this.appendFilterConditions(sql, request, context);  // where过滤
        this.appendGroupByConditions(sql, request);          // key列聚合
        this.appendFilterByHaving(sql, request, context);    // having过滤
        this.appendOrderByConditions(sql, request, context); // 排序列
        this.appendPageLimit(sql, request);                  // 分页

        return sql.toString();
    }

    // 构建Olap计数SQL
    private <T extends ItemAble> String buildCountOlapSql(OlapRequest<T> request) {
        StringBuilder sql = new StringBuilder();
        OlapContext context = new OlapCountContext(request);

        this.appendCountHeader(sql, request);                // 构造count头部
        this.appendPlaceholder(sql, context);                // 占位符#1
        this.appendQueryCols(sql, request, context);         // 查询列
        this.appendExtensionCols(sql, request, context);     // 扩展列
        this.appendPlaceholder(sql, context);                // 占位符#2
        this.appendTimeUnitCol(sql, request);                // 时间粒度
        this.appendTableName(sql, request);                  // 表名
        this.appendWhereConditions(sql, request);            // 查询条件
        this.appendFilterConditions(sql, request, context);  // 过滤
        this.appendGroupByConditions(sql, request);          // key列聚合
        this.appendFilterByHaving(sql, request, context);    // having过滤
        this.appendFilterNullValue(sql, request, context);   // 过滤空value行
        this.appendOrderByNull(sql, request);                // 强制不排序
        this.appendCountFooter(sql, request);                // 构造count尾部

        return this.optimizeCountSql(sql, request);          // 优化调整Sql
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, BigInteger>> getStorageDataByRequest(OlapRequest request) {
        this.lazyInit();
        if (!this.validateParameterByMapType(request)) {
            String errMsg = "OlapRequest format is wrong: " + request.toString();
            throw new OlapException(errMsg);
        }

        this.putDbShardKeyToThreadContext(request);   // db路由
        String sql = this.buildQueryOlapSql(request); // 构建sql
        LOG.info("[OLAP SQL] " + sql);

        return this.findBySQL(sql, Collections.emptyList(), new OlapMapTypeRowMapper());
    }

    public <N, T extends ItemAble> List<N> getAllUniqUserIds(OlapRequest<T> request, String indexTable) {
        this.lazyInit();
        String table = olapConfig.indexTable();
        if (indexTable != null) {
            table = indexTable;
        } else if (!StringUtils.isLegalConfValue(olapConfig.indexTable())) {
            String[] tables = request.getTable().split(OlapConstants.TABLE_DELIMITER);
            table = tables[0];
        }
        StringBuilder sql = new StringBuilder("SELECT DISTINCT(").append(olapConfig.userId())
                .append(") FROM ").append(olapConfig.database()).append(".").append(table).append(" WHERE ");
        this.appendDateCondition(sql, request);
        LOG.info("[OLAP SQL] " + sql);

        return this.getOneColumnBySqlOnAllShard(sql.toString());
    }

    private <T extends ItemAble, N extends Number> void putDbShardKeyToThreadContext(N userId) {
        ThreadContext.putOlapShardKey(userId);
    }

    private <T extends ItemAble> void putDbShardKeyToThreadContext(OlapRequest<T> request) {
        Number[] userIds = request.getUserIds();
        this.putDbShardKeyToThreadContext(userIds[0]);
    }

    /**
     * 过滤list中的NULL
     * 
     * @param result
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> List<T> filterNull(List<T> result) {
        if (null == result) {
            return result;
        }
        while (result.contains(null)) {
            result.remove(null);
        }
        return result;
    }

    /**
     * 请求体校验
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean validateParameter(OlapRequest<T> request) {
        if (null == request || ArrayUtils.isArrayEmpty(request.getUserIds()) || null == request.getItemClazz()
                || request.getTimeUnit() < 0) {
            return false;
        }
        return true;
    }

    /**
     * 请求体校验,返回Map格式
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean validateParameterByMapType(OlapRequest<T> request) {
        if (null == request || null == request.getUserIds() || ArrayUtils.isArrayEmpty(request.getUserIds())
                || request.getTimeUnit() < 0) {
            return false;
        }
        return true;
    }

    /**
     * 过滤条件中有value列
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean existValueColumnInFilter(OlapRequest<T> request) {
        String[] filters = request.getFilterConditions();
        if (ArrayUtils.isArrayEmpty(filters)) {
            return false; // 无过滤列
        }

        List<String> filterColumns = this.extractOlapColumn(filters);
        String[] basicVals = request.getBasicValueCols();

        @SuppressWarnings("unchecked")
        Collection<String> remainFilterColumns = CollectionUtils.subtract(filterColumns, Arrays.asList(basicVals));
        if (filterColumns.size() != remainFilterColumns.size()) {
            return true; // 有value列
        }

        Set<String> columnsInOlap = schemaLoader.getAllOlapColumns(request.getTable());
        if (columnsInOlap != null && columnsInOlap.containsAll(remainFilterColumns)) {
            return false; // 过滤列全为key列
        }
        return true;
    }

    /**
     * 是否为多用户查询
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean isMultiUserQuery(OlapRequest<T> request) {
        if (request.isMultiUser()) {
            return true;
        }
        if (request.getUserIds().length > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否为用户范围查询
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean isRangeUserQuery(OlapRequest<T> request) {
        if (request.isRangeUser()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 构造占位符
     * 
     * @param sql
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendPlaceholder(StringBuilder sql, OlapContext context) {
        sql.append("#");
        sql.append(context.placeholderNumber.addAndGet(1));
        sql.append("#");
    }

    /**
     * 构造需查询的数据列
     * 
     * @param sql
     * @param request
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendQueryCols(StringBuilder sql, OlapRequest<T> request, OlapContext context) {

        String[] basicValueCols = request.getBasicValueCols();
        for (String basicValue : basicValueCols) {
            sql.append("SUM(").append(basicValue).append(")").append(" AS ").append(context.alias(basicValue))
                    .append(",");
        }
        if (this.isMultiUserQuery(request) 
                && !ArrayUtils.arrayToList(request.getGroupByCols()).contains(olapConfig.userId())) {
            sql.append(olapConfig.userId()).append(",");
        }

        String[] groupByCols = request.getGroupByCols();
        if (ArrayUtils.isArrayNotEmpty(groupByCols) && context.isAliasColsEmpty()) {
            sql.append(StringUtils.makeStrFromCollection(Arrays.asList(groupByCols), ","));
            return;
        }
        if (ArrayUtils.isArrayNotEmpty(groupByCols) && !context.isAliasColsEmpty()) {
            String alias;
            for (String col : groupByCols) {
                alias = context.alias(col);
                if (alias.equals(col)) {
                    sql.append(col).append(",");
                } else {
                    sql.append(col).append(" AS ").append(context.alias(col)).append(",");
                }
            }
        }
        sql.deleteCharAt(sql.length() - 1);
    }

    /**
     * 构造扩展value列
     * 
     * @param sql
     * @param request
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void
            appendExtensionCols(StringBuilder sql, OlapRequest<T> request, OlapContext context) {
        String[] extensionCols = request.getExtensionCols();
        String[] extensionColComps = request.getExtensionColComps();

        if (ArrayUtils.isArrayEmpty(extensionCols) && ArrayUtils.isArrayEmpty(extensionColComps)) {
            return;
        }
        if (ArrayUtils.isArrayEmpty(extensionCols) || ArrayUtils.isArrayEmpty(extensionColComps)
                || extensionCols.length != extensionColComps.length) {
            LOG.error("extensionCols and extensionColComps is disMatch: " + String.valueOf(extensionCols)
                    + String.valueOf(extensionColComps));
            return;
        }

        String[] basicValueCols = request.getBasicValueCols();
        Set<String> basicValSet = new HashSet<String>();
        for (String basicValue : basicValueCols) {
            basicValSet.add(basicValue);
        }

        for (int i = 0; i < extensionCols.length; i++) {
            sql.append(",").append(buildColComp(extensionColComps[i], basicValSet, context)).append(" AS ")
                    .append(extensionCols[i]);
        }
    }

    /**
     * 构造复合value列
     * 
     * @param colComp
     * @param basicValSet
     * @param context
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private String buildColComp(String colComp, Set<String> basicValSet, OlapContext context) {
        if (CollectionUtils.isEmpty(basicValSet)) {
            return colComp;
        }
        String[] terms = colComp.split("\\W+");
        if (ArrayUtils.isArrayEmpty(terms)) {
            return colComp;
        }
        // 获取交集部分
        @SuppressWarnings("unchecked")
        List<String> intersec = (List<String>) CollectionUtils.intersection(Arrays.asList(terms), basicValSet);
        if (CollectionUtils.isEmpty(intersec)) {
            return colComp;
        }

        String[] symbol = colComp.split("\\w+");
        int len1 = terms.length;
        int len2 = symbol.length;
        int len = Math.max(len1, len2);
        boolean symbolFirst = terms[0].equals("") ? true : false;
        String result = "";

        for (int i = 0; i < len; i++) {
            if (i < len2 && !symbolFirst) {
                result += symbol[i];
            }
            if (i < len1) {
                if (intersec.contains(terms[i])) {
                    result += ("SUM(" + terms[i] + ")");
                } else {
                    result += terms[i];
                }
            }
            if (i < len2 && symbolFirst) {
                result += symbol[i];
            }
        }
        return result;
    }

    /**
     * 根据时间粒度构造时间聚合列
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendTimeUnitCol(StringBuilder sql, OlapRequest<T> request) {
        switch (request.getTimeUnit()) {
            case OlapConstants.TU_NONE:
                break;
            case OlapConstants.TU_HOUR:
                sql.append(",(UNIX_TIMESTAMP(").append(olapConfig.dateColumn()).append(")+hour*3600) AS groupTime");
                break;
            case OlapConstants.TU_DAY:
                sql.append(",UNIX_TIMESTAMP(").append(olapConfig.dateColumn()).append(") AS groupTime");
                break;
            case OlapConstants.TU_WEEK:
                sql.append(",UNIX_TIMESTAMP(TIMESTAMPADD(DAY,-WEEKDAY(").append(olapConfig.dateColumn())
                   .append("), ").append(olapConfig.dateColumn()).append(")) AS groupTime");
                break;
            case OlapConstants.TU_MONTH:
                sql.append(",UNIX_TIMESTAMP(DATE(CONCAT(YEAR(").append(olapConfig.dateColumn())
                   .append("),'-',MONTH(").append(olapConfig.dateColumn()).append("),'-1'))) AS groupTime");
                break;
            case OlapConstants.TU_QUART:
                sql.append(",UNIX_TIMESTAMP(DATE(CONCAT(YEAR(").append(olapConfig.dateColumn())
                   .append("),'-',(QUARTER(").append(olapConfig.dateColumn()).append(")*3-2),'-1'))) AS groupTime");
                break;
            case OlapConstants.TU_YEAR:
                sql.append(",UNIX_TIMESTAMP(DATE(CONCAT(YEAR(").append(olapConfig.dateColumn())
                   .append("),'-1','-1'))) AS groupTime");
                break;
            default:
                throw new OlapException("no support timeunit" + request.getTimeUnit());
        }
    }

    /**
     * 构造查询表
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendTableName(StringBuilder sql, OlapRequest<T> request) {
        String table = request.getTable();
        if (table.contains(OlapConstants.TABLE_DELIMITER)) {
            String[] tables = table.split(OlapConstants.TABLE_DELIMITER);
            String matchTable = null;
            for (String name : tables) {
                if (!this.isTableStrictMatch(name, request)) {
                    continue;
                }
                if (matchTable == null) {
                    matchTable = name;
                }
                if (OlapConstants.TU_HOUR == request.getTimeUnit() && name.startsWith("Hourly")) {
                    table = name;
                    break;
                } else if (OlapConstants.TU_DAY == request.getTimeUnit() && name.startsWith("Daily")) {
                    table = name;
                    break;
                } else if (OlapConstants.TU_MONTH == request.getTimeUnit() && name.startsWith("Monthly")) {
                    table = name;
                    break;
                } else if (OlapConstants.TU_YEAR == request.getTimeUnit() && name.startsWith("Yearly")) {
                    table = name;
                    break;
                } else if (OlapConstants.TU_NONE == request.getTimeUnit() && name.startsWith("Daily")) {
                    table = name;
                    break;
                }
            }
            // 未命中最优rollup表，则使用第一个
            if (table.contains(OlapConstants.TABLE_DELIMITER)) {
                table = matchTable == null ? tables[0] : matchTable;
            }
        }
        // 将查询table具体化
        request.setTable(table);
        String databaseName = StringUtils.isLegalConfValue(olapConfig.database()) ? olapConfig.database() : "olap";
        sql.append(" FROM ").append(databaseName).append(".").append(table);
    }

    /**
     * 校验请求在该表下查询是否可行
     * 
     * @param table
     * @param request
     * @return 查询是否可行
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean isTableStrictMatch(String table, OlapRequest<T> request) {
        Set<String> columns = schemaLoader.getAllOlapColumns(table);
        if (CollectionUtils.isEmpty(columns)) {
            return false;
        }

        String[] basicVals = request.getBasicValueCols();
        if (ArrayUtils.isArrayNotEmpty(basicVals)) {
            if (!columns.containsAll(Arrays.asList(basicVals))) {
                return false;
            }
        }

        String[] groupByCols = request.getGroupByCols();
        if (ArrayUtils.isArrayNotEmpty(groupByCols)) {
            if (!columns.containsAll(Arrays.asList(groupByCols))) {
                return false;
            }
        }

        String[] extColComps = request.getExtensionColComps();
        if (ArrayUtils.isArrayNotEmpty(extColComps)) {
            List<String> extColumns = this.extractOlapColumn(extColComps);
            if (!columns.containsAll(extColumns)) {
                return false;
            }
        }

        String[] filters = request.getFilterConditions();
        if (ArrayUtils.isArrayNotEmpty(filters)) {
            String[] extCols = request.getExtensionCols();
            List<String> filterColumns = this.extractOlapColumn(filters);
            filterColumns.removeAll(columns);
            if (ArrayUtils.isArrayNotEmpty(extCols)) {
                filterColumns.removeAll(Arrays.asList(extCols));
            }
            if (CollectionUtils.isNotEmpty(filterColumns)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 抽离出olap中的列名
     * 
     * @param columnComps
     * @return olap中的列名
     * @since 2015-7-28 by wangchongjie
     */
    private List<String> extractOlapColumn(String[] columnComps) {
        List<String> columns = new ArrayList<String>();
        if (ArrayUtils.isArrayEmpty(columnComps)) {
            return columns;
        }

        for (String filter : columnComps) {
            columns.addAll(this.splitToWord(filter));
        }
        columns.removeAll(OlapConstants.SQL_RESERVED_WORDS);
        return columns;
    }

    /**
     * 抽离出olap中的列名
     * @param columnStr
     * @return olap中的列名
     * @since 2015-7-28 by wangchongjie
     */
    private List<String> extractOlapColumn(String columnStr) {
        List<String> columns = new ArrayList<String>();
        if (StringUtils.isEmpty(columnStr)) {
            return columns;
        }
        columns.addAll(this.splitToWord(columnStr));
        columns.removeAll(OlapConstants.SQL_RESERVED_WORDS);
        return columns;
    }

    /**
     * 切分成单词
     * 
     * @param columnStr
     * @return 单词列表
     * @since 2015-7-28 by wangchongjie
     */
    private List<String> splitToWord(String columnStr) {
        List<String> columns = new ArrayList<String>();
        String[] terms = columnStr.split("\\W+");
        for (String term : terms) {
            if (!term.matches("^\\d+$")) {
                columns.add(term);
            }
        }
        return columns;
    }

    /**
     * 构造基础查询条件
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendWhereConditions(StringBuilder sql, OlapRequest<T> request) {
        sql.append(" WHERE ").append(olapConfig.userId());
        if (this.isRangeUserQuery(request)) {
            sql.delete(sql.length() - olapConfig.userId().length(), sql.length());
        } else if (this.isMultiUserQuery(request)) {
            sql.append(" in (").append(StringUtils.concatNumber(Arrays.asList(request.getUserIds()))).append(") AND");
        } else {
            sql.append(" = ").append(request.getUserIds()[0]).append(" AND");
        }

        this.appendDateCondition(sql, request);
    }

    /**
     * 构造时间条件
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendDateCondition(StringBuilder sql, OlapRequest<T> request) {
        FastDateFormat f = FastDateFormat.getInstance("yyyy-MM-dd");
        Date startDate = this.getTableStartDateInConf(request);
        Date[] ensuredDate = DateUtils.ensureDate(request.getFrom(), request.getTo(), startDate);

        sql.append(" ").append(olapConfig.dateColumn()).append(" >= \'").append(f.format(ensuredDate[0].getTime()))
           .append("\' AND ").append(olapConfig.dateColumn()).append(" < \'")
           .append(f.format(DateUtils.addDays(ensuredDate[1], 1).getTime())).append("\'");
    }

    /**
     * 获取起始查询时间
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> Date getTableStartDateInConf(OlapRequest<T> request) {

        Date startDate = olapConfig.getTableStartDate(request.getTable());
        return startDate;
    }

    /**
     * 构造where过滤条件
     * 
     * @param sql
     * @param request
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendFilterConditions(StringBuilder sql, OlapRequest<T> request,
            OlapContext context) {
        String[] filterConditions = request.getFilterConditions();
        List<String> filterValueConditions = context.filterValueConditions;
        if (ArrayUtils.isArrayEmpty(filterConditions)) {
            return;
        }
        Set<String> filterConditionSet = new HashSet<String>(Arrays.asList(filterConditions));
        for (String filter : filterConditionSet) {
            if (isFilterForValue(filter, request, context)) {
                filterValueConditions.add(this.buildAliasFilter(filter, context));
            } else {
                sql.append(" AND ").append(filter);
            }
        }
    }

    /**
     * 构造别名过滤条件
     * 
     * @param filter
     * @param context
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private String buildAliasFilter(String filter, OlapContext context) {
        if (context.isAliasColsEmpty()) {
            return filter;
        }

        String[] terms = filter.split("\\W+");
        if (ArrayUtils.isArrayEmpty(terms)) {
            return filter;
        }

        // 获取交集部分
        @SuppressWarnings("unchecked")
        List<String> intersec =
                (List<String>) CollectionUtils.intersection(Arrays.asList(terms), context.getOriAliasCol());
        if (CollectionUtils.isEmpty(intersec)) {
            return filter;
        }

        String[] symbol = filter.split("\\w+");
        int len1 = terms.length;
        int len2 = symbol.length;
        int len = Math.max(len1, len2);
        boolean symbolFirst = terms[0].equals("") ? true : false;
        String result = "";

        for (int i = 0; i < len; i++) {
            if (i < len2 && !symbolFirst) {
                result += symbol[i];
            }
            if (i < len1) {
                if (intersec.contains(terms[i])) {
                    result += (context.alias(terms[i]));
                } else {
                    result += terms[i];
                }
            }
            if (i < len2 && symbolFirst) {
                result += symbol[i];
            }
        }
        return result;
    }

    /**
     * 构造having过滤条件
     * 
     * @param sql
     * @param request
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendFilterByHaving(StringBuilder sql, OlapRequest<T> request,
            OlapContext context) {
        List<String> filterValueConditions = context.filterValueConditions;
        if (CollectionUtils.isEmpty(filterValueConditions)) {
            return;
        }
        sql.append(" HAVING ");
        for (String filter : filterValueConditions) {
            sql.append(filter).append(" AND ");
        }
        sql.delete(sql.length() - 5, sql.length() - 1);
    }

    /**
     * 构造过滤value全空记录行
     * 
     * @param sql
     * @param request
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendFilterNullValue(StringBuilder sql, OlapRequest<T> request,
            OlapContext context) {
        List<String> filterValueConditions = context.filterValueConditions;
        if (CollectionUtils.isEmpty(filterValueConditions)) {
            sql.append(" HAVING ");
        } else {
            sql.append(" AND ");
        }
        String[] basicValueCols = request.getBasicValueCols();
        for (String basicValue : basicValueCols) {
            sql.append("SUM(").append(basicValue).append(")").append("+");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(">0");
    }

    /**
     * 构造group by语句
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendGroupByConditions(StringBuilder sql, OlapRequest<T> request) {
        String[] groupByCols = request.getGroupByCols();
        boolean isMultiUser = this.isMultiUserQuery(request);
        boolean isGroupByTime = OlapConstants.TU_NONE != request.getTimeUnit();

        if (ArrayUtils.isArrayNotEmpty(groupByCols) || isGroupByTime || isMultiUser) {
            sql.append(" GROUP BY ");
            if (ArrayUtils.isArrayNotEmpty(groupByCols)) {
                for (String by : groupByCols) {
                    sql.append(by).append(",");
                }
            }
            if (isMultiUser) {
                sql.append(olapConfig.userId()).append(",");
            }
            if (OlapConstants.TU_NONE != request.getTimeUnit()) {
                sql.append("groupTime,");
            }
            sql.deleteCharAt(sql.length() - 1);
        }
    }

    /**
     * 避免group by的filesort开销,大查询约有4%的性能提升
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendOrderByNull(StringBuilder sql, OlapRequest<T> request) {
        sql.append(" ORDER BY NULL");

    }

    /**
     * 构造排序条件
     * 
     * @param sql
     * @param request
     * @param context
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendOrderByConditions(StringBuilder sql, OlapRequest<T> request,
            OlapContext context) {

        List<Pair<String, SortOrder>> orderPairs = request.getOrderPairs();
        if (CollectionUtils.isEmpty(orderPairs)) {
            return;
        }

        Set<String> columns = schemaLoader.getAllOlapColumns(request.getTable());
        String[] extCols = request.getExtensionCols();

        boolean hasEffectOrderByColumn = false;

        for (Pair<String, SortOrder> pair : orderPairs) {
            String orderBy = pair.field1;
            SortOrder order = pair.field2;

            boolean isColumnOk = false;
            if (CollectionUtils.isNotEmpty(columns) && columns.contains(orderBy)) {
                isColumnOk = true;
            }
            if (ArrayUtils.isArrayNotEmpty(extCols) && Arrays.asList(extCols).contains(orderBy)) {
                isColumnOk = true;
            }
            if (!isColumnOk) {
                continue;
            }
            if (!StringUtils.isEmpty(orderBy)) {
                if (!hasEffectOrderByColumn) {
                    sql.append(" ORDER BY ");
                }
                sql.append(context.alias(orderBy));
                sql.append(" ");
                sql.append(order);
                sql.append(",");
                hasEffectOrderByColumn = true;
            }
        }
        if (hasEffectOrderByColumn) {
            sql.deleteCharAt(sql.length() - 1);
        }
    }

    /**
     * 构造分页
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendPageLimit(StringBuilder sql, OlapRequest<T> request) {
        int page = request.getPage();
        int pageSize = request.getPageSize();
        if (page >= 0 && pageSize > 0) {
            sql.append(" LIMIT ").append(page * pageSize).append(", ").append(pageSize);
        }
    }

    /**
     * 调整优化计数Sql
     * 
     * @param sql
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> String optimizeCountSql(StringBuilder sql, OlapRequest<T> request) {
        boolean canNotOptimize = this.existValueColumnInFilter(request);
        String resultSql;
        if (canNotOptimize) {
            resultSql = sql.toString().replaceAll("#.*?#", "");
        } else {
            resultSql = sql.toString().replaceAll("#.*#", "1");
        }
        return resultSql;
    }

    /**
     * 构造count sql头部
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendCountHeader(StringBuilder sql, OlapRequest<T> request) {
        sql.append("SELECT COUNT(1) FROM (SELECT ");
    }

    /**
     * 构造count sql尾部
     * 
     * @param sql
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void appendCountFooter(StringBuilder sql, OlapRequest<T> request) {
        sql.append(") T");
    }

    /**
     * 是否为涉及value的filter
     * 
     * @param filterStr
     * @param request
     * @param context
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> boolean
            isFilterForValue(String filterStr, OlapRequest<T> request, OlapContext context) {
        String[] basicVals = request.getBasicValueCols();
        String[] extenVals = request.getExtensionCols();

        List<String> basicValList = ArrayUtils.arrayToList(basicVals);
        List<String> extenValList = ArrayUtils.arrayToList(extenVals);

        List<String> filterColumns = this.extractOlapColumn(filterStr);
        for (String column : filterColumns) {
            if (basicValList.contains(column) || extenValList.contains(column)) {
                return true;
            }
        }

        if (!context.isAliasColsEmpty()) {
            for (String column : filterColumns) {
                if (basicValList.contains(context.deAlias(column))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 若为多用户查询，则user需在同一个sharding上，userId传任意一个
     */
    @Override
    public <N extends Number> List<Map<String, BigInteger>> getStorageDataBySQL(String sql, N userId) {
        return this.getStorageDataBySQL(sql, Collections.emptyList(), userId);
    }

    /**
     * 若为多用户查询，则user需在同一个sharding上，userId传任意一个
     */
    @Override
    @SuppressWarnings("unchecked")
    public <N extends Number, O> List<Map<String, BigInteger>>
            getStorageDataBySQL(String sql, List<O> params, N userId) {
        this.putDbShardKeyToThreadContext(userId);
        return this.findBySQLReturnMap(sql, params, new OlapMapTypeRowMapper());
    }

    /**
     * 在所有分片上获取一列数据
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getOneColumnBySqlOnAllShard(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return Collections.EMPTY_LIST;
        }
        Set<Number> userIdDelegate = schemaLoader.getUserIdDelegate();
        List<T> result = new ArrayList<T>();
        for (Number userId : userIdDelegate) {
            this.putDbShardKeyToThreadContext(userId);
            result.addAll(this.findByGeneralSQL(sql, Collections.emptyList(), new OlapOneColumnRowMapper<T>()));
        }
        return result;
    }

    /**
     * 在一个随机分片上执行sql
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> executeSQLonOneRandomShard(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return Collections.EMPTY_LIST;
        }
        this.putDbShardKeyToThreadContext((int) (Math.random() * Integer.MAX_VALUE));
        return this.findByGeneralSQL(sql, Collections.emptyList(), new GeneralRowMapper());
    }

    /**
     * 在一个确定分片上执行sql
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> executeSQLonSameShard(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return Collections.EMPTY_LIST;
        }
        this.putDbShardKeyToThreadContext((int) (1));
        return this.findByGeneralSQL(sql, Collections.emptyList(), new GeneralRowMapper());
    }

}