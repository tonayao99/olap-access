package com.baidu.unbiz.olap.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OlapEngine中字段常量
 * 
 * @author wangchongjie
 * @fileName OlapConstants.java
 * @dateTime 2014-1-3 下午2:59:19
 */
public class OlapConstants {

    // 时间单位
    public static final int TU_NONE = 0;
    public static final int TU_HOUR = 1;
    public static final int TU_DAY = 2;
    public static final int TU_WEEK = 3;
    public static final int TU_MONTH = 4;
    public static final int TU_QUART = 5;
    public static final int TU_YEAR = 6;

    // 是否开启报表数据异步缓存
    public static final boolean CACHE_ENABLE = false;
    public static final String TABLE_DELIMITER = ",";

    // sql保留字
    private static final List<String> INTERNAL_SQL_RESERVED_WORDS = Arrays.asList(new String[] { 
            "in", "not", "div", "mod", "or", "and", "add", "xor", "dual", "both", "exists", "label", "union" });

    public static final List<String> SQL_RESERVED_WORDS = new ArrayList<String>(INTERNAL_SQL_RESERVED_WORDS);
    
    static {
        for (String str : INTERNAL_SQL_RESERVED_WORDS) {
            SQL_RESERVED_WORDS.add(str.toUpperCase());
        }
    }

    // 特殊字段
    public static final class COLUMN {
        public static final String IGNORE = "ignore"; // olap填充时，忽略该字段
        public static final String TIME = "groupTime"; // 聚合时间字段
        public static final String SHOWDATE = "showDate"; // 显示时间字段
    }

    public static final int BATCH_QUERY_SIZE = 10;
}
