package com.baidu.unbiz.olap.driver.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.obj.Pair;

/**
 * 向OLAP发送的请求
 */
public class OlapRequest<T extends ItemAble> {

    /**
     * OlapEngine接口返回的对象格式
     */
    private Class<? extends ItemAble> itemClazz;

    /**
     * userIds
     */
    private Number[] userIds;

    /**
     * olap表名
     */
    private String table;

    /**
     * 起始查询日期
     */
    private Date from;

    /**
     * 终止查询日期
     */
    private Date to;

    /**
     * 查询的时间粒度
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
     * 基础value列对应的字串列表
     */
    private String[] basicValueCols;

    /**
     * group by列名
     */
    private String[] groupByCols;

    /**
     * olap列别名
     */
    private String[] aliasCols;

    /**
     * 扩展value列 对应的字串列表
     */
    private String[] extensionCols;

    /**
     * 扩展列求值方法列表
     */
    private String[] extensionColComps;

    /**
     * 过滤条件
     */
    private String[] filterConditions;

    /**
     * 排序列&排序向
     */
    private List<Pair<String, SortOrder>> orderPairs;

    /**
     * 是否为多用户查询
     */
    private boolean isMultiUser = false;

    /**
     * 是否为范围用户查询,可无user
     */
    private boolean isRangeUser = false;

    public boolean isRangeUser() {
        return isRangeUser;
    }

    public void setRangeUser(boolean isRangeUser) {
        this.isRangeUser = isRangeUser;
    }

    public boolean isMultiUser() {
        return isMultiUser;
    }

    public void setMultiUser(boolean isMultiUser) {
        this.isMultiUser = isMultiUser;
    }

    public Number[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Number[] userIds) {
        this.userIds = userIds;
    }

    public Class<? extends ItemAble> getItemClazz() {
        return itemClazz;
    }

    public void setItemClazz(Class<? extends ItemAble> itemClazz) {
        this.itemClazz = itemClazz;
    }

    public String[] getFilterConditions() {
        return filterConditions;
    }

    public void setFilterConditions(String[] filterConditions) {
        this.filterConditions = filterConditions;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String[] getBasicValueCols() {
        return basicValueCols;
    }

    public void setBasicValueCols(String[] basicValueCols) {
        this.basicValueCols = basicValueCols;
    }

    public String[] getGroupByCols() {
        return groupByCols;
    }

    public void setGroupByCols(String[] groupByCols) {
        this.groupByCols = groupByCols;
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

    public String[] getExtensionCols() {
        return extensionCols;
    }

    public void setExtensionCols(String[] extensionCols) {
        this.extensionCols = extensionCols;
    }

    public String[] getExtensionColComps() {
        return extensionColComps;
    }

    public void setExtensionColComps(String[] extensionColComps) {
        this.extensionColComps = extensionColComps;
    }

    public List<Pair<String, SortOrder>> getOrderPairs() {
        return orderPairs;
    }

    public void setOrderPairs(List<Pair<String, SortOrder>> orderPairs) {
        this.orderPairs = orderPairs;
    }

    public String[] getAliasCols() {
        return aliasCols;
    }

    public void setAliasCols(String[] aliasCols) {
        this.aliasCols = aliasCols;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
