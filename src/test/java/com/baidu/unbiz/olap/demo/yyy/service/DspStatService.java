package com.baidu.unbiz.olap.demo.yyy.service;

import java.util.Date;
import java.util.List;

import com.baidu.unbiz.olap.demo.yyy.bo.MultiUserStatDayItem;
import com.baidu.unbiz.olap.demo.yyy.bo.StatDayItem;

/**
 * Function: 查询DSP统计数据；
 *
 * @author   <a href="mailto:wangchongjie@baidu.com">王崇杰</a>
 * @created  2014-1-4
 * @since    adx 1.0
 */
public interface DspStatService {

    List<StatDayItem> queryADspData(int dspId, Date from, Date to, String column, int order, 
    		int timeUnit, int page, int pageSize);
    
    int countADspData(int dspId, Date from, Date to, String column, int order, 
    		int timeUnit, int page, int pageSize);
    
    List<MultiUserStatDayItem> queryMultiDspData(List<Integer> dspIds, Date from, Date to, String column, int order, 
    		int timeUnit, int page, int pageSize);
    
    List<StatDayItem> queryRangeDspData(Date from, Date to, String column, int order, 
            int timeUnit, int page, int pageSize);
}
