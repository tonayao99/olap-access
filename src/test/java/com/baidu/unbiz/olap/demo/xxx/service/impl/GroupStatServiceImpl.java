package com.baidu.unbiz.olap.demo.xxx.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.demo.xxx.bo.GroupViewItem;
import com.baidu.unbiz.olap.demo.xxx.constant.XXX;
import com.baidu.unbiz.olap.demo.xxx.service.GroupRequest;
import com.baidu.unbiz.olap.demo.xxx.service.GroupStatService;
import com.baidu.unbiz.olap.demo.xxx.service.XxxAbstractOlapService;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequestBuilder;
import com.baidu.unbiz.olap.utils.OlapUtils;

@Service
public class GroupStatServiceImpl extends XxxAbstractOlapService implements GroupStatService {

    @Override
    public List<GroupViewItem> queryGroupData(GroupRequest req) {
        List<String> filters = new LinkedList<String>();
        OlapUtils.makeNumberFilter(filters, XXX.COLUMN.PLANID, req.getPlanIds());
        OlapUtils.makeNumberFilter(filters, XXX.COLUMN.GROUPID, req.getGroupIds());

        ReportRequest<GroupViewItem> rr =
                new ReportRequestBuilder<GroupViewItem>() {
                }.setUserId(req.getUserId()).setFrom(req.getFrom()).setTo(req.getTo()).setTimeUnit(req.getTimeUnit())
                        .setOrderPairs(req.getOrderPairs()).setFilters(filters).build();

        return super.getStorageData(rr);
    }

    @Override
    public List<GroupViewItem> queryGroupDataByKeyValSpecify(GroupRequest req) {
        List<String> filters = new LinkedList<String>();
        OlapUtils.makeNumberFilter(filters, XXX.COLUMN.PLANID, req.getPlanIds());
        OlapUtils.makeNumberFilter(filters, XXX.COLUMN.GROUPID, req.getGroupIds());

        ReportRequest<GroupViewItem> rr =
                new ReportRequestBuilder<GroupViewItem>() {
                }.setUserId(req.getUserId()).setFrom(req.getFrom()).setTo(req.getTo()).setTimeUnit(req.getTimeUnit())
                        .setOrderPairs(req.getOrderPairs()).setFilters(filters)
                        .setForceKeyVal(new String[] { XXX.COLUMN.PLANID }).build();

        return super.getStorageData(rr);
    }

    @Override
    public List<GroupViewItem> queryBatchGroupData(GroupRequest req) {

        ReportRequest<GroupViewItem> rr = new ReportRequestBuilder<GroupViewItem>() {
        }.setUserId(req.getUserId()).setFrom(req.getFrom()).setTo(req.getTo()).setTimeUnit(req.getTimeUnit()).build();

        int batchSize = 1000;
        return super.getBatchStorageData(rr, batchSize);
    }

}
