package com.baidu.unbiz.olap.demo.yyy.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.demo.yyy.bo.AdSizeStatItem;
import com.baidu.unbiz.olap.demo.yyy.service.AdSizeStatService;
import com.baidu.unbiz.olap.demo.yyy.service.YyyAbstractOlapService;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequestBuilder;
import com.baidu.unbiz.olap.service.FileWritterResultHandler;

/**
 * 应用方可抽象一层 XxxAbstractOlapService,其继承自AbstractOlapService,封装业务定制功能
 * 
 * @author wangchongjie
 * @fileName DspStatServiceImpl2.java
 * @dateTime 2013-12-11 上午11:29:43
 */
@Service
public class AdSizeStatServiceImpl extends YyyAbstractOlapService implements AdSizeStatService {

    @Override
    public List<AdSizeStatItem> queryAdSizeData(int dspId, Date from, Date to, String column, int order, int timeUnit,
            int page, int pageSize) {

        ReportRequest<AdSizeStatItem> rr = new ReportRequestBuilder<AdSizeStatItem>() { }
            .setUserId(dspId).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(page).setPageSize(pageSize).build();

        return super.getStorageData(rr);
    }

    @Override
    public int countAdSizeData(int dspId, Date from, Date to, String column, int order, int timeUnit, int page,
            int pageSize) {

        ReportRequest<AdSizeStatItem> rr = new ReportRequestBuilder<AdSizeStatItem>() { }
            .setUserId(dspId).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(pageSize).setPageSize(pageSize).build();

        return super.countStorageData(rr);
    }

    @Override
    public List<AdSizeStatItem> queryBatchAdSizeData(Date from, Date to, String column, int order, int timeUnit,
            int page, int pageSize) {

        ReportRequest<AdSizeStatItem> rr = new ReportRequestBuilder<AdSizeStatItem>() { }
           .setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
           .setIndexTable("DailyPlanStats").build();

        return super.getBatchStorageData(rr, 1000);
    }

    @Override
    public void queryBatchAdSizeData2File(Date from, Date to, String column, int order, int timeUnit) {

        ReportRequest<AdSizeStatItem> rr = new ReportRequestBuilder<AdSizeStatItem>() {
        }.setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
        // .setIndexTable("DailyPlanStats") 可以执行索引表
                .build();

        FileWritterResultHandler<AdSizeStatItem> handler = new FileWritterResultHandler<AdSizeStatItem>("adSizeFile") {
            @Override
            public String getItemContent(AdSizeStatItem item) {
                return item.getShowDate() + "\t" + item.getClksAlias() + "\t" + item.getCost();
            }
        };

        super.getBatchStorageData(rr, 1000, handler);
    }
}
