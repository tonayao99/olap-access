package com.baidu.unbiz.olap.demo.zzz.palo.constant;

/**
 * ZZZ产品线PALO中字段常量 该类应由olap-access模块的调用者实现
 * 
 * @author wangchongjie
 * @fileName ZZZ.java
 * @dateTime 2015-7-30 上午10:21:34
 */
public class ZZZ {

    // 列名
    public static final class COLUMN {
        // 特殊字段
        public static final String IGNORE = "ignore"; // olap填充时忽略该字段
        public static final String USERID = "UserId"; // olap主key id
        public static final String TIME = "groupTime"; // 聚合时间字段
        // 数据字段
        public static final String PV = "pv"; // 展现
        public static final String UV = "uv"; // UV
        public static final String UVPVR = "uvpvr";

        // 主键列字段
        public static final String SITEID = "siteid"; // 网站ID
        public static final String SITENAME = "sitename"; // 网站名称

    }

    // 扩展列表达式
    public static final class EXPR {
        public static final String UVPVR = COLUMN.UV + "/" + COLUMN.PV + "/1";
    }

    // olap表名
    public static final class TABLE {
        // 基础数据单表
        public static final String SITE = "dsp_site_stats";
    }

}
