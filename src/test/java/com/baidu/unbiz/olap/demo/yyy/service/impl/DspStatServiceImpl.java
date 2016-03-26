package com.baidu.unbiz.olap.demo.yyy.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.demo.yyy.bo.MultiUserStatDayItem;
import com.baidu.unbiz.olap.demo.yyy.bo.StatDayItem;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;
import com.baidu.unbiz.olap.demo.yyy.service.DspStatService;
import com.baidu.unbiz.olap.demo.yyy.service.YyyAbstractOlapService;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequestBuilder;
import com.baidu.unbiz.olap.obj.Pair;
import com.baidu.unbiz.olap.utils.OlapUtils;

/**
 * 应用方可抽象一层 XxxAbstractOlapService,其继承自AbstractOlapService,封装业务定制功能
 * 
 * @author wangchongjie
 * @fileName DspStatServiceImpl.java
 * @dateTime 2013-12-11 上午11:29:43
 */
@Service
public class DspStatServiceImpl extends YyyAbstractOlapService implements DspStatService {

    // 数据入doris年月日，之前的数据不提供查询，建议优先使用配置，而非重写该方法
    // @Override
    // protected DateTriple getStartDate(){
    // return new DateTriple(2014, 1, 1);
    // }

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
        // OlapUtils.makeNumberRangeFilter(filters, YYY.COLUMN.DSPID, 9, CompareType.NEQ);

        ReportRequest<StatDayItem> rr = new ReportRequestBuilder<StatDayItem>() {
        }.setFrom(from).setTo(to).setTimeUnit(timeUnit).setFilters(filters).build();

        return super.getStorageData(rr);
    }

    @Override
    public int countADspData(int dspId, Date from, Date to, String column, int order, int timeUnit, int page,
            int pageSize) {

        ReportRequest<StatDayItem> rr = new ReportRequestBuilder<StatDayItem>() {
        }.setUserId(dspId).setFrom(from).setTo(to).setTimeUnit(timeUnit)
                // 以下注释列即使设置框架也会忽略
                // .setOrderBy(column)
                // .setOrder(order)
                // .setPage(pageSize)
                // .setPageSize(pageSize)
                .build();

        return super.countStorageData(rr);
    }

    @Override
    public List<MultiUserStatDayItem> queryMultiDspData(List<Integer> dspIds, Date from, Date to, String column,
            int order, int timeUnit, int page, int pageSize) {

        // 支持多重排序
        @SuppressWarnings("unchecked")
        List<Pair<String, SortOrder>> orderPairs =
                Pair.wrapList(new Pair<String, SortOrder>(YYY.COLUMN.SRCHS, SortOrder.ASC),
                        new Pair<String, SortOrder>(YYY.COLUMN.CLKS, SortOrder.DESC));

        ReportRequest<MultiUserStatDayItem> rr = new ReportRequestBuilder<MultiUserStatDayItem>() { }
            .setUserIds(dspIds).setFrom(from).setTo(to).setOrderBy(column).setOrderPairs(orderPairs)
            .setPage(pageSize).setPageSize(pageSize).build();

        return super.getStorageData(rr);
    }

}
