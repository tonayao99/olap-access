package com.baidu.unbiz.olap.driver.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.obj.BaseItem;
import com.baidu.unbiz.olap.obj.Pair;

/**
 * 报表请求构造器，NotThreadSafe
 * 
 * @author wangchongjie
 * @fileName ReportRequestBuilder.java
 * @dateTime 2015-7-15 下午7:04:32
 */
public abstract class ReportRequestBuilder<T extends BaseItem> extends ReportRequestBuilderBase<T> {
    
    /**
     * 过滤条件
     */
    private List<String> filters;
    
    /**
     * userIds
     */
    private List<? extends Number> userIds;
    
    /**
     * userId
     */
    private Number userId;
    
    /**
     *  起始查询日期
     */
    private Date from;
    
    /**
     * 终止查询日期
     */
    private Date to;
    
    /**
     * 排序列
     */
    private String orderBy;
    
    /**
     * 排序向
     */
    private SortOrder order;
    
    /**
     * 时间粒度
     */
    private int timeUnit;
    
    /**
     * 第几页
     */
    private int page;
    
    /**
     * 每页大小
     */
    private int pageSize;
    /**
     * 支持多重排序,格式为：排序列->排序向 List
     */
    private List<Pair<String, SortOrder>> orderPairs;

    /**
     * 是否启动多olap表查询
     */
    private boolean multiTable = false;

    /**
     * 强制查询某张olap表，此时忽略item的注解配置表
     */
    private String forceTable;
    
    /**
     * 强制按指定key列聚合
     */
    private List<String> forceKeyVal;
    
    /**
     * 强制不使用某些key列,执行的优先级高于forceKeyVal
     */
    private List<String> forceUnUseKeyVal;

    /**
     * 强制使用某个index表，配合getBatchStorageData接口使用
     */
    protected String indexTable;

    public ReportRequestBuilder<T> setIndexTable(String indexTable) {
        this.indexTable = indexTable;
        return this;
    }

    public ReportRequestBuilder<T> setForceTable(String forceTable) {
        this.forceTable = forceTable;
        return this;
    }

    public ReportRequestBuilder<T> setMultiTable(boolean multiTable) {
        this.multiTable = multiTable;
        return this;
    }

    public ReportRequestBuilder<T> setFilters(List<String> filters) {
        this.filters = filters;
        return this;
    }

    public ReportRequestBuilder<T> addFilters(List<String> filters) {
        if (this.filters == null) {
            this.filters = filters;
        } else {
            this.filters.addAll(filters);
        }
        return this;
    }

    public ReportRequestBuilder<T> addForceKeyVal(String...forceKeyVal) {
        if (this.forceKeyVal == null) {
            this.forceKeyVal = new LinkedList<String>();
        }
        for (String key : forceKeyVal) {
            this.forceKeyVal.add(key);
        }
        return this;
    }

    public ReportRequestBuilder<T> setForceKeyVal(List<String> forceKeyVal) {
        this.forceKeyVal = forceKeyVal;
        return this;
    }

    public ReportRequestBuilder<T> setForceKeyVal(String[] forceKeyVal) {
        if (forceKeyVal == null) {
            return this;
        }
        this.forceKeyVal = Arrays.asList(forceKeyVal);
        return this;
    }

    public ReportRequestBuilder<T> setUserIds(List<? extends Number> userIds) {
        this.userIds = userIds;
        return this;
    }

    public ReportRequestBuilder<T> setUserId(Number userId) {
        this.userId = userId;
        return this;
    }

    public ReportRequestBuilder<T> setFrom(Date from) {
        this.from = from;
        return this;
    }

    public ReportRequestBuilder<T> setTo(Date to) {
        this.to = to;
        return this;
    }

    public ReportRequestBuilder<T> setOrderBy(String orderBycolumn) {
        this.orderBy = orderBycolumn;
        return this;
    }

    public ReportRequestBuilder<T> setOrder(int order) {
        this.order = SortOrder.val(order);
        return this;
    }

    public ReportRequestBuilder<T> setOrder(String order) {
        this.order = SortOrder.val(order);
        return this;
    }

    public ReportRequestBuilder<T> setOrder(SortOrder order) {
        this.order = order;
        return this;
    }

    public ReportRequestBuilder<T> setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public ReportRequestBuilder<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public ReportRequestBuilder<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ReportRequestBuilder<T> setOrderPairs(List<Pair<String, SortOrder>> orderPairs) {
        this.orderPairs = orderPairs;
        return this;
    }
    

    public List<String> getForceUnUseKeyVal() {
        return forceUnUseKeyVal;
    }

    public ReportRequestBuilder<T>  setForceUnUseKeyVal(List<String> forceRemoveKeyVal) {
        this.forceUnUseKeyVal = forceRemoveKeyVal;
        return this;
    }

    public ReportRequest<T> build() {
        ReportRequest<T> rr;
        if (entityClass != null) {
            rr = new ReportRequest<T>(entityClass) {
            };
        } else {
            rr = new ReportRequest<T>() {
            };
        }

        List<Number> uids = new ArrayList<Number>();
        if (CollectionUtils.isEmpty(userIds)) {
            if (userId != null) {
                uids.add(userId);
            }
        } else {
            uids.addAll(userIds);
        }

        if (CollectionUtils.isNotEmpty(orderPairs)) {
            rr.setOrderPairs(orderPairs);
        }
        if (StringUtils.isNotEmpty(orderBy) && order != null && CollectionUtils.isEmpty(orderPairs)) {
            @SuppressWarnings("unchecked")
            List<Pair<String, SortOrder>> orderPairs = Pair.wrapList(new Pair<String, SortOrder>(orderBy, order));
            rr.setOrderPairs(orderPairs);
        }

        rr.setUserIds(uids);
        rr.setFilters(filters);
        rr.setFrom(from);
        rr.setTo(to);
        rr.setTimeUnit(timeUnit);
        rr.setPage(page);
        rr.setPageSize(pageSize);
        rr.setMultiTable(multiTable);
        rr.setForceTable(forceTable);
        rr.setForceKeyVal(forceKeyVal);
        rr.setForceUnUseKeyVal(forceUnUseKeyVal);
        rr.setIndexTable(indexTable);

        return rr;
    }
}
