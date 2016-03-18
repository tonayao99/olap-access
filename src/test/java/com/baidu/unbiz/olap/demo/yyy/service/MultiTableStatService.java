package com.baidu.unbiz.olap.demo.yyy.service;

import java.util.Date;
import java.util.List;

import com.baidu.unbiz.olap.demo.yyy.bo.MultiStatItem;

public interface MultiTableStatService {

    // 推荐使用:多Olap表查询
    List<MultiStatItem> queryMultiStatData(int dspId, Date from, Date to, String column, int order, int timeUnit,
            int page, int pageSize);

    // 推荐使用:多Olap表配置，指定单表查询
    List<MultiStatItem> queryUvStatData(int dspId, Date from, Date to, String column, int order, int timeUnit,
            int page, int pageSize);

    // 推荐使用：多用户、多Olap表查询
    List<MultiStatItem> queryMultiStatDataByMultiUser(List<Integer> dspIds, Date from, Date to, String column,
            int order, int timeUnit, int page, int pageSize);
}
