package com.baidu.unbiz.olap.demo.testcase.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.demo.yyy.bo.AdSizeStatItem;
import com.baidu.unbiz.olap.demo.yyy.bo.MultiStatItem;
import com.baidu.unbiz.olap.demo.yyy.bo.MultiUserStatDayItem;
import com.baidu.unbiz.olap.demo.yyy.bo.StatDayItem;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;
import com.baidu.unbiz.olap.demo.yyy.service.AdSizeStatService;
import com.baidu.unbiz.olap.demo.yyy.service.DspStatService;
import com.baidu.unbiz.olap.demo.yyy.service.MultiTableStatService;
import com.baidu.unbiz.olap.demo.yyy.service.impl.DspStatServiceWithCacheImpl;

/**
 * olap service测试类
 * 
 * @author wangchongjie
 * @fileName OlapServiceTest.java
 * @dateTime 2014-6-24 下午5:34:52
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class OlapYyyServiceTest {

    @Resource(name = "multiTableStatServiceImpl")
    private MultiTableStatService service;

    @Resource(name = "dspStatServiceImpl")
    private DspStatService dspService;

    @Resource(name = "dspStatServiceWithCacheImpl")
    private DspStatServiceWithCacheImpl dspMgr;

    @Resource(name = "adSizeStatServiceImpl")
    private AdSizeStatService adSizeMgr;

    // 推荐：使用Builder模式，查询单用户数据
    @Test
    public void testQueryByBuilder() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<StatDayItem> data = dspService.queryADspData(dspId, from, to, null, 0, OlapConstants.TU_DAY, 0, 0);

        for (StatDayItem item : data) {
            System.out.println(item.toString());
        }
        System.out.println(data == null ? 0 : data.size());
    }

    // 推荐：使用Builder模式，查询一个范围的用户数据
    @Test
    public void testQueryRangeUserByBuilder() {
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<StatDayItem> data = dspService.queryRangeDspData(from, to, null, 0, OlapConstants.TU_DAY, 0, 0);

        for (StatDayItem item : data) {
            System.out.println(item.toString());
        }
        System.out.println(data == null ? 0 : data.size());
    }

    // 推荐：使用Builder模式，查询单用户数据
    @Test
    public void testCountByBuilder() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        int data = dspService.countADspData(dspId, from, to, null, 0, OlapConstants.TU_DAY, 0, 0);
        System.out.println(data);
    }

    // 推荐：使用Builder模式，查询多用户数据
    @Test
    public void testMultiUserQueryInSameSharding() {
        List<Integer> dspIds = new ArrayList<Integer>();
        dspIds.addAll(Arrays.asList(new Integer[] { 62, 63 }));
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<MultiUserStatDayItem> data =
                dspService.queryMultiDspData(dspIds, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);

        for (MultiUserStatDayItem item : data) {
            System.out.println(item.toString());
        }
    }

    // 推荐：使用Builder模式，查询多用户数据，单表数据
    @Test
    public void testMultiUserQueryInDifferentSharding() {
        List<Integer> dspIds = new ArrayList<Integer>();
        dspIds.addAll(Arrays.asList(new Integer[] { 8, 62, 63, 64 }));
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<MultiUserStatDayItem> data =
                dspService.queryMultiDspData(dspIds, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);

        for (MultiUserStatDayItem item : data) {
            System.out.println(item.toString());
        }
    }

    // 推荐：使用Builder模式，查询单用户，多Olap表数据
    @Test
    public void testMultiOlapTableQuery() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<MultiStatItem> data =
                service.queryMultiStatData(dspId, from, to, YYY.COLUMN.ADSIZEID, 1, OlapConstants.TU_NONE, 0, 5);
        System.out.println(data);
    }

    // 推荐：使用Builder模式，查询单用户，多Olap表数据
    @Test
    public void testMultiOlapTableByUserSpecifyOneTableQuery() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<MultiStatItem> data =
                service.queryUvStatData(dspId, from, to, YYY.COLUMN.ADSIZEID, 1, OlapConstants.TU_NONE, 0, 5);
        System.out.println(data);
    }

    // 推荐：使用Builder模式，查询多用户，多Olap表数据
    @Test
    public void testMultiOlapTableByMultiUserQuery() {
        List<Integer> dspIds = new ArrayList<Integer>();
        dspIds.addAll(Arrays.asList(new Integer[] { 8, 62, 63, 64 }));

        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<MultiStatItem> data =
                service.queryMultiStatDataByMultiUser(dspIds, from, to, null, 1, OlapConstants.TU_DAY, 0, 0);
        for (MultiStatItem item : data) {
            System.out.println(item);
        }
    }

    // 推荐：Olap缓存功能测试，
    @Test
    public void testQueryByBuilderWithCache() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<StatDayItem> data = dspMgr.queryADspData(dspId, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);
        data = dspMgr.queryADspData(dspId, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);
        data = dspMgr.queryADspData(dspId, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);
        data = dspMgr.queryADspData(dspId, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);
        data = dspMgr.queryADspData(dspId, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);

        for (StatDayItem item : data) {
            System.out.println(item.toString());
        }

        List<StatDayItem> dataNoCache = dspService.queryADspData(dspId, from, to, null, OlapConstants.TU_DAY, 0, 0, 0);
        Assert.isTrue(dataNoCache.toString().equals(data.toString()));
    }

    // 推荐：Olap缓存功能测试
    @Test
    public void testCountByBuilderWithCache() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        int count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_DAY, 0, 0);
        count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_MONTH, 0, 0);
        count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_HOUR, 0, 0);
        count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_WEEK, 0, 0);
        count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_DAY, 0, 0);
        count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_DAY, 0, 0);
        count = dspMgr.countADspData(dspId, from, to, null, 0, OlapConstants.TU_DAY, 0, 0);

        System.out.println(count);
    }

    // 推荐: 建模带别名alias方式查询，支持单表与多表
    @Test
    public void testQueryWithAliasByBuilder() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<AdSizeStatItem> data =
                adSizeMgr.queryAdSizeData(dspId, from, to, YYY.COLUMN.CLKS, 0, OlapConstants.TU_DAY, 0, 0);

        for (AdSizeStatItem item : data) {
            System.out.println(item.toString());
        }
        System.out.println(data == null ? 0 : data.size());
    }

    // 推荐: 建模带别名alias方式查询，支持单表与多表
    @Test
    public void testCountWithAliasByBuilder() {
        Integer dspId = 8;
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        int data = adSizeMgr.countAdSizeData(dspId, from, to, YYY.COLUMN.CLKS, 0, OlapConstants.TU_DAY, 0, 0);

        System.out.println(data);
    }

    // 推荐: 批量全库数据查询
    @Test
    public void testBatchQueryByBuilder() {
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        List<AdSizeStatItem> data =
                adSizeMgr.queryBatchAdSizeData(from, to, YYY.COLUMN.CLKS, 0, OlapConstants.TU_DAY, 0, 0);

        for (AdSizeStatItem item : data) {
            System.out.println(item.toString());
        }
        System.out.println(data == null ? 0 : data.size());
    }

    // 推荐: 批量查询并将结果导出至文件
    @Test
    public void testBatchQuery2File() {
        Date from = newDate(2015, 7, 1);
        Date to = newDate(2015, 7, 30);

        adSizeMgr.queryBatchAdSizeData2File(from, to, YYY.COLUMN.CLKS, 0, OlapConstants.TU_DAY);

    }

    private Date newDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
