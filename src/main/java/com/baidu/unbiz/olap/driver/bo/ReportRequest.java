package com.baidu.unbiz.olap.driver.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.obj.Pair;

/**
 * 报表查询请求抽象类
 * 
 * @author wangchongjie
 * @fileName ReportRequest.java
 * @dateTime 2015-7-28 下午5:02:27
 */
public abstract class ReportRequest<T extends ItemAble> extends BaseRequest<T> {
   
    /**
     * 过滤条件
     */
    protected List<String> filters;

    /**
     * userIds
     */
    protected List<Number> userIds;
    
    /**
     * 起始查询日期
     */
    protected Date from;
    
    /**
     * 终止查询日期
     */
    protected Date to;
    
    /**
     * 时间粒度
     */
    protected int timeUnit;
    
    /**
     * 第几页，从0开始
     */
    protected int page = -1;
    
    /**
     * 每页大小
     */
    protected int pageSize = 0;

    /**
     * 支持多重排序,格式为：排序列->排序向
     */
    protected List<Pair<String, SortOrder>> orderPairs;

    /**
     * 是否多olap表查询
     */
    protected boolean multiTable = false;

    /**
     * 强制查询某张olap表
     */
    protected String forceTable;
    
    /**
     * 强制按指定key列聚合
     */
    protected List<String> forceKeyVal;
    
    /**
     * 强制去除某些key列,执行的优先级高于forceKeyVal
     */
    protected List<String> forceUnUseKeyVal;

    /**
     * 强制使用某个index表
     */
    protected String indexTable;

    public ReportRequest() {
    }

    public ReportRequest(Class<T> itemClazz) {
        super.entityClass = itemClazz;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<Number> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Number> userIds) {
        this.userIds = userIds;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isMultiTable() {
        return multiTable;
    }

    public void setMultiTable(boolean multiTable) {
        this.multiTable = multiTable;
    }

    public String getForceTable() {
        return forceTable;
    }

    public void setForceTable(String forceTable) {
        this.forceTable = forceTable;
    }

    public String getIndexTable() {
        return indexTable;
    }

    public void setIndexTable(String indexTable) {
        this.indexTable = indexTable;
    }

    public List<Pair<String, SortOrder>> getOrderPairs() {
        return orderPairs;
    }

    public void setOrderPairs(List<Pair<String, SortOrder>> orderPairs) {
        this.orderPairs = orderPairs;
    }
    
    public List<String> getForceKeyVal() {
        return forceKeyVal;
    }

    public void setForceKeyVal(List<String> forceKeyVal) {
        this.forceKeyVal = forceKeyVal;
    }
    
    public List<String> getForceUnUseKeyVal() {
        return forceUnUseKeyVal;
    }

    public void setForceUnUseKeyVal(List<String> forceRemoveKeyVal) {
        this.forceUnUseKeyVal = forceRemoveKeyVal;
    }
}
