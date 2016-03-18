package com.baidu.unbiz.olap.obj;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.FastDateFormat;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapMergeKey;
import com.baidu.unbiz.olap.constant.OlapConstants;

/**
 * 所有olapEngine bo的父类
 * 
 * @author wangchongjie
 * @fileName BaseItem.java
 * @dateTime 2014-1-3 下午6:48:39
 */
public abstract class BaseItem implements ItemAble {

    private static final long serialVersionUID = 1L;

    @OlapColumn(OlapConstants.COLUMN.TIME)
    protected long unixTime;

    @OlapMergeKey
    protected String showDate;

    protected static FastDateFormat sd1 = FastDateFormat.getInstance("yyyy.MM.dd");
    protected static FastDateFormat sd2 = FastDateFormat.getInstance("yyyy.MM.dd HH");
    protected static FastDateFormat sd3 = FastDateFormat.getInstance("HH : 00");
    protected static FastDateFormat sd4 = FastDateFormat.getInstance("yyyy-MM-dd");
    protected static FastDateFormat sd5 = FastDateFormat.getInstance("yyyyMMdd");

    /**
     * 可被子类覆盖,ORM完成之前调用此方法,运行且仅运行一次
     */
    public void afterAssemble(int timeUnit) {
        this.buildShowDate(timeUnit);
    }

    /**
     * 可被子类覆盖,多表merger之后被callback,运行且仅运行一次
     */
    public void afterMultiTableQueryAssemble() {
        // do something after multi-table merge
    }

    /**
     * 构建显示日期列
     * 
     * @param timeUnit
     * @since 2015-7-28 by wangchongjie
     */
    protected void buildShowDate(int timeUnit) {
        this.buildShowDate(timeUnit, sd1);
    }

    /**
     * 构建显示日期
     * 
     * @param timeUnit
     * @param dayFormat
     * @since 2015-7-28 by wangchongjie
     */
    protected void buildShowDate(int timeUnit, FastDateFormat dayFormat) {
        if (this.unixTime > 0) {
            Date d = new Date();
            d.setTime(this.unixTime * 1000L);
            if (OlapConstants.TU_DAY == timeUnit) { // 天粒度
                this.showDate = dayFormat.format(d);
            } else if (OlapConstants.TU_HOUR == timeUnit) {
                this.showDate = sd3.format(d);
            } else {
                this.showDate = dayFormat.format(d);
            }
        }
    }

    public BaseItem() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long time) {
        this.unixTime = time;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

}
