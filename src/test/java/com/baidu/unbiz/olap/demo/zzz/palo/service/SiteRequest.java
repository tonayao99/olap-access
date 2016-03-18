package com.baidu.unbiz.olap.demo.zzz.palo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.obj.Pair;

/**
 * 网站维度请求对象
 * 
 * @author wangchongjie
 * @fileName SiteRequest.java
 * @dateTime 2015-7-30 上午10:22:52
 */
public class SiteRequest {

    protected Date from;
    protected Date to;
    protected int timeUnit;
    protected List<Pair<String, SortOrder>> orderPairs;
    // 北斗四层级字段
    protected Integer userId;
    // 多用户查询使用
    protected List<Integer> userIds;


    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public List<Pair<String, SortOrder>> getOrderPairs() {
        return orderPairs;
    }

    public void setOrderPairs(List<Pair<String, SortOrder>> orderPairs) {
        this.orderPairs = orderPairs;
    }

    public void setOrderPairs(Pair<String, SortOrder>...orderPairs) {
        this.orderPairs = new ArrayList<Pair<String, SortOrder>>();
        this.orderPairs.addAll(Arrays.asList(orderPairs));
    }

}
