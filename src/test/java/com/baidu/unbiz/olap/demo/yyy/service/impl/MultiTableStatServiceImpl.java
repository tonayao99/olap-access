package com.baidu.unbiz.olap.demo.yyy.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.demo.yyy.bo.MultiStatItem;
import com.baidu.unbiz.olap.demo.yyy.service.MultiTableStatService;
import com.baidu.unbiz.olap.demo.yyy.service.YyyAbstractOlapService;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequestBuilder;

/**
 * 多olap表查询封装
 * 
 * @author wangchongjie
 * @fileName MultiTableStatServiceImpl.java
 * @dateTime 2014-10-8 上午11:55:46
 */
@Service
public class MultiTableStatServiceImpl extends YyyAbstractOlapService implements MultiTableStatService {

    // 推荐实现方式
    public List<MultiStatItem> queryMultiStatData(int dspId, Date from, Date to, String column, int order,
            int timeUnit, int page, int pageSize) {

        ReportRequest<MultiStatItem> rr = new ReportRequestBuilder<MultiStatItem>() { }
            .setUserId(dspId).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(page).setPageSize(pageSize).setMultiTable(true).build();

        return super.getStorageData(rr);
    }

    // 推荐实现方式
    public List<MultiStatItem> queryMultiStatDataByMultiUser(List<Integer> dspIds, Date from, Date to, String column,
            int order, int timeUnit, int page, int pageSize) {

        ReportRequest<MultiStatItem> rr = new ReportRequestBuilder<MultiStatItem>() { }
            .setUserIds(dspIds).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(pageSize).setPageSize(pageSize).setMultiTable(true).build();

        return super.getStorageData(rr);
    }

    @Override
    public List<MultiStatItem> queryUvStatData(int dspId, Date from, Date to, String column, int order, int timeUnit,
            int page, int pageSize) {

        ReportRequest<MultiStatItem> rr = new ReportRequestBuilder<MultiStatItem>() { }
            .setUserId(dspId).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(page).setPageSize(pageSize).setForceTable("DailyAdSizeUvStats").build();

        return super.getStorageData(rr);
    }
}
