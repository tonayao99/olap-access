package com.baidu.unbiz.olap.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 数字工具类
 * 
 * @author wangchongjie
 * @fileName NumberUtil.java
 * @dateTime 2013-7-27 上午1:42:08
 */
public class NumberUtil {

    public static final int SCALE = 4;

    public static float formatFloat(float f, int scale) {
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static double formatDouble(double f, int scale) {
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal doubleToBigDecimal(double dd) {
        BigDecimal bd;
        try {
            bd = BigDecimal.valueOf(dd).setScale(SCALE, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            bd = BigDecimal.valueOf(0d);
        }
        return bd;
    }

    public static BigDecimal doubleToBigDecimal(double dd, int scale) {
        BigDecimal bd;
        try {
            bd = BigDecimal.valueOf(dd).setScale(scale, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            bd = BigDecimal.valueOf(0d);
        }
        return bd;
    }

    // 格式化为千分位
    public static String format2Thousands(Number n) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(n);
    }

    public static int[] toArray(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new int[0];
        }

        int[] arr = new int[list.size()];
        for (int i = 0, j = list.size(); i < j; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println(NumberUtil.formatFloat(12.34567f, 2));
        System.out.println(NumberUtil.formatDouble(12.34567d, 4));
        System.out.println(NumberUtil.format2Thousands(1234567));
    }

}
