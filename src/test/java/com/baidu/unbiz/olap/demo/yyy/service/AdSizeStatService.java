package com.baidu.unbiz.olap.demo.yyy.service;

import java.util.Date;
import java.util.List;

import com.baidu.unbiz.olap.demo.yyy.bo.AdSizeStatItem;

/**
 * Function: 查询创意尺寸统计数据；
 *
 * @author   <a href="mailto:wangchongjie@baidu.com">王崇杰</a>
 * @created  2014-1-4
 * @since    adx 1.0
 */
public interface AdSizeStatService {

    List<AdSizeStatItem> queryAdSizeData(int dspId, Date from, Date to, String column, int order, 
    		int timeUnit, int page, int pageSize);
    
    int countAdSizeData(int dspId, Date from, Date to, String column, int order, 
            int timeUnit, int page, int pageSize);
    
    List<AdSizeStatItem> queryBatchAdSizeData(Date from, Date to, String column, int order, 
            int timeUnit, int page, int pageSize);
    
    void queryBatchAdSizeData2File(Date from, Date to, String column, int order, int timeUnit);
}
