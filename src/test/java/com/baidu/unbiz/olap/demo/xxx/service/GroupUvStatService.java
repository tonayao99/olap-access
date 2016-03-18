package com.baidu.unbiz.olap.demo.xxx.service;

import java.util.List;

import com.baidu.unbiz.olap.demo.xxx.bo.GroupViewItem;

public interface GroupUvStatService {

    /**
     * 查询推广组维度数据
     * 
     * @param req
     * @return
     * @since 2015-2-5 下午1:15:59 created by wangchongjie
     */
    List<GroupViewItem> queryGroupData(GroupRequest req);
    
}
