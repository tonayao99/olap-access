package com.baidu.unbiz.olap.demo.zzz.palo.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.demo.zzz.palo.bo.SiteViewItem;
import com.baidu.unbiz.olap.demo.zzz.palo.service.SiteRequest;
import com.baidu.unbiz.olap.demo.zzz.palo.service.SiteStatService;
import com.baidu.unbiz.olap.demo.zzz.palo.service.ZzzAbstractOlapService;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.driver.bo.ReportRequestBuilder;

@Service
public class SiteStatServiceImpl extends ZzzAbstractOlapService implements SiteStatService {

    @Override
    public List<SiteViewItem> querySiteData(SiteRequest req) {
        List<String> filters = new LinkedList<String>();
        
        ReportRequest<SiteViewItem> rr = new ReportRequestBuilder<SiteViewItem>() { }
                .setUserId(req.getUserId())
                .setFrom(req.getFrom())
                .setTo(req.getTo())
                .setTimeUnit(req.getTimeUnit())
                .setOrderPairs(req.getOrderPairs())
                .setFilters(filters)
                .build();

        return super.getStorageData(rr);
    }
}
