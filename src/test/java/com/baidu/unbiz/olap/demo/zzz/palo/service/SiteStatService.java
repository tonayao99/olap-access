package com.baidu.unbiz.olap.demo.zzz.palo.service;

import java.util.List;

import com.baidu.unbiz.olap.demo.zzz.palo.bo.SiteViewItem;

public interface SiteStatService {

    /**
     * 查询网站维度数据
     * 
     * @param req
     * @return
     * @since 2015-7-30 by wangchongjie
     */
    List<SiteViewItem> querySiteData(SiteRequest req);

}
