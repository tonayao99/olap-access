package com.baidu.unbiz.olap.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.demo.yyy.bo.MultiStatItem;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;

/**
 * SchemaLoader测试类
 * 
 * @author wangchongjie
 * @fileName SchemaLoaderTest.java
 * @dateTime 2014-7-16 下午6:58:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-olap.xml")
public class ReportUtilsTest {

    @Test
    public void testMerge() {

        List<MultiStatItem> aL = new ArrayList<MultiStatItem>();
        MultiStatItem a = new MultiStatItem();
        a.setAdSizeId(100L);
        a.setUnixTime(11);
        a.setSrchs(9);
        aL.add(a);

        List<MultiStatItem> bL = new ArrayList<MultiStatItem>();
        MultiStatItem b = new MultiStatItem();
        b.setAdSizeId(100L);
        b.setClks(6);
        b.setCost(3);
        b.setUnixTime(11);
        bL.add(b);
        MultiStatItem b2 = new MultiStatItem();
        b2.setAdSizeId(102L);
        b2.setClks(62);
        b2.setCost(32);
        bL.add(b2);

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(YYY.COLUMN.ADSIZEID);
        mergeKeys.add(OlapConstants.COLUMN.TIME);

        // 获取次表的value字段
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(YYY.COLUMN.COST);
        mergeVals.add(YYY.COLUMN.CLKS);

        List<MultiStatItem> cL = ReportUtils.mergeItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, true);

        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            cL = ReportUtils.mergeItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, true);
        }
        System.out.println(System.currentTimeMillis() - time);

        System.out.println(cL);
    }

    @Test
    public void testJoin() {

        List<MultiStatItem> aL = new ArrayList<MultiStatItem>();
        MultiStatItem a = new MultiStatItem();
        a.setAdSizeId(100L);
        a.setUnixTime(11);
        a.setSrchs(9);
        aL.add(a);

        List<MultiStatItem> bL = new ArrayList<MultiStatItem>();
        MultiStatItem b = new MultiStatItem();
        b.setAdSizeId(100L);
        b.setClks(6);
        b.setCost(3);
        b.setUnixTime(11);
        bL.add(b);
        MultiStatItem b2 = new MultiStatItem();
        b2.setAdSizeId(102L);
        b2.setClks(62);
        b2.setCost(32);
        bL.add(b2);

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(YYY.COLUMN.ADSIZEID);
        mergeKeys.add(OlapConstants.COLUMN.TIME);

        // 获取次表的value字段
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(YYY.COLUMN.COST);
        mergeVals.add(YYY.COLUMN.CLKS);

        List<MultiStatItem> cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, true);

        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, true);
        }
        System.out.println(System.currentTimeMillis() - time);

        System.out.println(cL);
    }

    @Test
    public void testMergeItemListKeepSubListOrder() {

        List<MultiStatItem> aL = new ArrayList<MultiStatItem>();
        MultiStatItem a = new MultiStatItem();
        a.setAdSizeId(100L);
        a.setUnixTime(11);
        a.setSrchs(9);
        aL.add(a);

        List<MultiStatItem> bL = new ArrayList<MultiStatItem>();
        MultiStatItem b2 = new MultiStatItem();
        b2.setAdSizeId(102L);
        b2.setClks(62);
        b2.setCost(32);
        bL.add(b2);
        MultiStatItem b = new MultiStatItem();
        b.setAdSizeId(100L);
        b.setClks(6);
        b.setCost(3);
        b.setUnixTime(11);
        bL.add(b);

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(YYY.COLUMN.ADSIZEID);
        mergeKeys.add(OlapConstants.COLUMN.TIME);

        // 获取次表的value字段
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(YYY.COLUMN.COST);
        mergeVals.add(YYY.COLUMN.CLKS);

        List<MultiStatItem> cL =
                ReportUtils.mergeItemListKeepSubListOrder(aL, bL, mergeKeys, mergeVals, MultiStatItem.class,
                        SortOrder.val("DESC"), true);

        System.out.println(cL);
    }

    @Test
    public void testJoinItemListKeepSubListOrder() {

        List<MultiStatItem> aL = new ArrayList<MultiStatItem>();
        MultiStatItem a = new MultiStatItem();
        a.setAdSizeId(100L);
        a.setUnixTime(11);
        a.setSrchs(9);
        aL.add(a);

        List<MultiStatItem> bL = new ArrayList<MultiStatItem>();
        MultiStatItem b2 = new MultiStatItem();
        b2.setAdSizeId(102L);
        b2.setClks(62);
        b2.setCost(32);
        bL.add(b2);
        MultiStatItem b = new MultiStatItem();
        b.setAdSizeId(100L);
        b.setClks(6);
        b.setCost(3);
        b.setUnixTime(11);
        bL.add(b);

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(YYY.COLUMN.ADSIZEID);
        mergeKeys.add(OlapConstants.COLUMN.TIME);

        // 获取次表的value字段
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(YYY.COLUMN.COST);
        mergeVals.add(YYY.COLUMN.CLKS);

        List<MultiStatItem> cL =
                ReportUtils.joinItemListKeepSubListOrder(aL, bL, mergeKeys, mergeVals, MultiStatItem.class,
                        SortOrder.val("DESC"), true);

        System.out.println(cL);
    }

    @Test
    public void testMergePerformance() {

        List<MultiStatItem> aL = new ArrayList<MultiStatItem>();
        MultiStatItem a = new MultiStatItem();
        a.setAdSizeId(100L);
        a.setUnixTime(11);
        a.setSrchs(9);
        aL.add(a);
        MultiStatItem aa;
        for (int i = 0; i < 10000; i++) {
            aa = new MultiStatItem();
            aa.setAdSizeId(100L + i);
            aa.setUnixTime(11);
            aa.setSrchs(9);
            aL.add(aa);
        }

        List<MultiStatItem> bL = new ArrayList<MultiStatItem>();
        MultiStatItem b = new MultiStatItem();
        b.setAdSizeId(100L);
        b.setClks(6);
        b.setCost(3);
        b.setUnixTime(11);
        bL.add(b);
        MultiStatItem b2 = new MultiStatItem();
        b2.setAdSizeId(102L);
        b2.setClks(62);
        b2.setCost(32);
        bL.add(b2);
        MultiStatItem bb;
        for (int i = 10000; i > 0; i--) {
            bb = new MultiStatItem();
            bb.setAdSizeId(100L + i);
            bb.setUnixTime(11);
            bb.setClks(62);
            bb.setCost(32);
            bL.add(bb);
        }

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(YYY.COLUMN.ADSIZEID);
        mergeKeys.add(OlapConstants.COLUMN.TIME);

        // 获取次表的value字段
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(YYY.COLUMN.COST);
        mergeVals.add(YYY.COLUMN.CLKS);

        @SuppressWarnings("unused")
        List<MultiStatItem> cL = ReportUtils.mergeItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, false);

        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, false);
        }
        System.out.println(System.currentTimeMillis() - time);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, false);
        }

        System.out.println(System.currentTimeMillis() - time);
        // System.out.println(cL);
    }

    @Test
    public void testJoinPerformance() {

        List<MultiStatItem> aL = new ArrayList<MultiStatItem>();
        MultiStatItem a = new MultiStatItem();
        a.setAdSizeId(100L);
        a.setUnixTime(11);
        a.setSrchs(9);
        aL.add(a);
        MultiStatItem aa;
        for (int i = 0; i < 10000; i++) {
            aa = new MultiStatItem();
            aa.setAdSizeId(100L + i);
            aa.setUnixTime(11);
            aa.setSrchs(9);
            aL.add(aa);
        }

        List<MultiStatItem> bL = new ArrayList<MultiStatItem>();
        MultiStatItem b = new MultiStatItem();
        b.setAdSizeId(100L);
        b.setClks(6);
        b.setCost(3);
        b.setUnixTime(11);
        bL.add(b);
        MultiStatItem b2 = new MultiStatItem();
        b2.setAdSizeId(102L);
        b2.setClks(62);
        b2.setCost(32);
        bL.add(b2);
        MultiStatItem bb;
        for (int i = 10000; i > 0; i--) {
            bb = new MultiStatItem();
            bb.setAdSizeId(100L + i);
            bb.setUnixTime(11);
            bb.setClks(62);
            bb.setCost(32);
            bL.add(bb);
        }

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(YYY.COLUMN.ADSIZEID);
        mergeKeys.add(OlapConstants.COLUMN.TIME);

        // 获取次表的value字段
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(YYY.COLUMN.COST);
        mergeVals.add(YYY.COLUMN.CLKS);

        @SuppressWarnings("unused")
        List<MultiStatItem> cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, false);
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, false);
        }

        System.out.println(System.currentTimeMillis() - time);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            cL = ReportUtils.joinItemList(aL, bL, mergeKeys, mergeVals, MultiStatItem.class, false);
        }

        System.out.println(System.currentTimeMillis() - time);
        // System.out.println(cL);
    }
}
