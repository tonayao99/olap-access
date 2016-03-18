package com.baidu.unbiz.olap.service;

import java.util.List;

import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * OlapService业务接口
 * 
 * @author wangchongjie
 * @fileName OlapService.java
 * @dateTime 2014-9-17 下午2:18:38
 */
public interface OlapService {

    /**
     * 从olap中查询所需item列表
     * 
     * @param rr
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    <T extends ItemAble> List<T> getStorageData(ReportRequest<T> rr);

    /**
     * 获取olap表符合条件的记录数
     * 
     * @param rr
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    <T extends ItemAble> int countStorageData(ReportRequest<T> rr);

    /**
     * 批量获取数据，扫全库user数据
     * 
     * @param rr
     * @param batchSize
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    <T extends ItemAble> List<T> getBatchStorageData(ReportRequest<T> rr, int batchSize);

    /**
     * 批量获取数据，扫全库user数据, 自定义结果处理方式
     * 
     * @param rr
     * @param batchSize
     * @param handler
     * @since 2015-7-28 by wangchongjie
     */
    <T extends ItemAble> void getBatchStorageData(ReportRequest<T> rr, int batchSize, ResultHandler<T> handler);
}
