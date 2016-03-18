package com.baidu.unbiz.olap.demo.xxx.service;

import java.util.List;

import com.baidu.unbiz.olap.demo.xxx.bo.GroupViewItem;

public interface GroupStatService {

    /**
     * 查询推广组维度数据
     * 
     * @param req
     * @return
     * @since 2015-2-5 下午1:15:59 created by wangchongjie
     */
    List<GroupViewItem> queryGroupData(GroupRequest req);

    /**
     * 查询推广组维度数据,显式指定聚合列
     * 
     * @param req
     * @return
     * @author wangchongjie
     * @since 2015-9-7
     */
    List<GroupViewItem> queryGroupDataByKeyValSpecify(GroupRequest req);
    
    /**
     * 批量/全量数据查询
     * 
     * @param req
     * @return
     * @author wangchongjie
     * @since 2015-10-15
     */
    List<GroupViewItem> queryBatchGroupData(GroupRequest req);
}
