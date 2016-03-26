package com.baidu.unbiz.olap.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * Created by wangchongjie on 16/3/25.
 */
public class H2Support {

    private FastDateFormat format = FastDateFormat.getInstance("yyyy-mm-dd");

    public static long getUnixTime(Date time) {
//        if (time instanceof java.sql.Timestamp) {
//            return getSeconds((java.sql.Timestamp) time);
//        } else if (time instanceof java.sql.Date) {
            return getSeconds((java.sql.Date) time);
//        } else {
//            return getSeconds((String) time);
//        }
    }

    public static long getSeconds(java.sql.Timestamp ts) {
        return ts.getTime() / 1000;
    }

    public static long getSeconds(java.sql.Date date) {
        return date.getTime() / 1000;
    }

    public static long getSeconds(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        java.util.Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    public static Date getDate(char[] dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf.parse(dateString.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(date.getTime());
    }

    public static String concat(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }
}
