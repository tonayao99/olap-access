package com.baidu.unbiz.olap.demo.yyy.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.cache.OlapCacheHandler;
import com.baidu.unbiz.olap.cache.impl.SimpleOlapCacheHandler;
import com.baidu.unbiz.olap.demo.yyy.bo.MultiUserStatDayItem;
import com.baidu.unbiz.olap.demo.yyy.bo.StatDayItem;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;
import com.baidu.unbiz.olap.demo.yyy.service.DspStatService;
import com.baidu.unbiz.olap.demo.yyy.service.YyyAbstractOlapService;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequestBuilder;
import com.baidu.unbiz.olap.util.OlapUtils;

/**
 * 应用方可抽象一层 YyyAbstractOlapService,其继承自AbstractOlapService,封装业务定制功能
 * 
 * @author wangchongjie
 * @since 2013-12-11 上午11:29:43
 */
@Service
public class DspStatServiceWithCacheImpl extends YyyAbstractOlapService implements DspStatService {

    // 用户可按场景自行实现handler，完成Olap数据缓存异步存储及查询
    @Override
    protected OlapCacheHandler getOlapCacheHandler() {
        return SimpleOlapCacheHandler.getInstance();
    }

    @Override
    public List<StatDayItem> queryADspData(int dspId, Date from, Date to, String column, int order, int timeUnit,
            int page, int pageSize) {

        ReportRequest<StatDayItem> rr = new ReportRequestBuilder<StatDayItem>() { }
            .setUserId(dspId).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(pageSize).setPageSize(pageSize).build();

        return super.getStorageData(rr);
    }

    @Override
    public List<StatDayItem> queryRangeDspData(Date from, Date to, String column, int order, int timeUnit, int page,
            int pageSize) {

        List<String> filters = new LinkedList<String>();
        OlapUtils.makeNumberRangeFilter(filters, YYY.COLUMN.DSPID, 1, 10000);

        ReportRequest<StatDayItem> rr = new ReportRequestBuilder<StatDayItem>() { }
            .setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit).setFilters(filters)
            .setPage(pageSize).setPageSize(pageSize).build();

        return super.getStorageData(rr);
    }

    @Override
    public List<MultiUserStatDayItem> queryMultiDspData(List<Integer> dspIds, Date from, Date to, String column,
            int order, int timeUnit, int page, int pageSize) {

        ReportRequest<MultiUserStatDayItem> rr = new ReportRequestBuilder<MultiUserStatDayItem>() { }
            .setUserIds(dspIds).setFrom(from).setTo(to).setOrderBy(column).setOrder(order).setTimeUnit(timeUnit)
            .setPage(pageSize).setPageSize(pageSize).build();

        return super.getStorageData(rr);
    }

    @Override
    public int countADspData(int dspId, Date from, Date to, String column, int order, int timeUnit, int page,
            int pageSize) {

        ReportRequest<StatDayItem> rr = new ReportRequestBuilder<StatDayItem>() { }
            .setUserId(dspId).setFrom(from).setTo(to).setTimeUnit(timeUnit)
            // 以下注释列即使设置框架也会忽略
            // .setOrderBy(column)
            // .setOrder(order)
            // .setPage(pageSize)
            // .setPageSize(pageSize)
            .build();

        return super.countStorageData(rr);
    }

}
