package com.baidu.unbiz.olap.demo.xxx.bo;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.demo.xxx.constant.XXX;
import com.baidu.unbiz.olap.obj.BaseItem;

/**
 * Xxx产品线报表效果数据通用基类
 * 
 * @author wangchongjie
 * @fileName StatInfo.java
 * @dateTime 2015-7-17 下午2:43:39
 */
public class StatInfo extends BaseItem {
    private static final long serialVersionUID = 1L;

    @OlapColumn(XXX.COLUMN.SRCHS)
    protected long srchs; // 展现

    @OlapColumn(XXX.COLUMN.CLKS)
    protected long clks; // 点击

    @OlapColumn(XXX.COLUMN.COST)
    protected double cost; // 单位元

    @OlapColumn(XXX.COLUMN.CTR)
    protected BigDecimal ctr; // 点击率

    @OlapColumn(XXX.COLUMN.ACP)
    protected BigDecimal acp; // 平均点击价格

    @OlapColumn(XXX.COLUMN.CPM)
    protected BigDecimal cpm; // 千次展现成本

    @OlapColumn(XXX.COLUMN.SRCHUV)
    protected long srchuv; // 展现受众

    @OlapColumn(XXX.COLUMN.CLKUV)
    protected long clkuv; // 点击受众

    @OlapColumn(XXX.COLUMN.TRANSUV)
    protected long transuv; // 转化受众

    @OlapColumn(XXX.COLUMN.DIRECTTRANS)
    protected long directTrans; // 直接转化数

    @OlapColumn(XXX.COLUMN.INDIRECTTRANS)
    protected long indirectTrans; // 间接转化数

    @OlapColumn(value = XXX.COLUMN.CLKS, alias = XXX.COLUMN.HOLMESCLKS)
    protected long holmesClks; // holmes点击数

    @OlapColumn(XXX.COLUMN.ARRIVALCNT)
    protected long arrivalCnt; // 到达数

    @OlapColumn(XXX.COLUMN.EFFECTARRCNT)
    protected long effectArrCnt; // 有效到达数，去掉停留时间为0的过滤后的值

    @OlapColumn(XXX.COLUMN.HOPCNT)
    protected long hopCnt; // 二跳数

    @OlapColumn(XXX.COLUMN.RESTIME)
    protected int resTime; // 停留时间，秒

    protected BigDecimal arrivalRate; // 到达率
    protected BigDecimal hopRate; // 二跳率
    protected BigDecimal avgResTime; // 平均停留时间，秒
    protected String resTimeStr; // 停留时间，字符串，格式为“HH:mm:ss”

    protected BigDecimal srsur; // 展现频次:展现次数/展现受众
    protected BigDecimal cusur; // 受众点击率：点击受众/展现受众
    protected BigDecimal cocur; // 平均受众点击价格：消费/点击受众

    @Override
    public void afterAssemble(int timeUnit) {
        super.buildShowDate(timeUnit, sd4);
        this.buildCostField();
    }

    private void buildCostField() {
        // 注意：由于对象中存储的cost是元，而record中的是分,因此要除100
        this.cost = this.cost / 100.0d;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /************* getter & setter  *************/
    public long getSrchs() {
        return srchs;
    }

    public void setSrchs(long srchs) {
        this.srchs = srchs;
    }

    public long getClks() {
        return clks;
    }

    public void setClks(long clks) {
        this.clks = clks;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public BigDecimal getCtr() {
        return ctr;
    }

    public void setCtr(BigDecimal ctr) {
        this.ctr = ctr;
    }

    public BigDecimal getAcp() {
        return acp;
    }

    public void setAcp(BigDecimal acp) {
        this.acp = acp;
    }

    public BigDecimal getCpm() {
        return cpm;
    }

    public void setCpm(BigDecimal cpm) {
        this.cpm = cpm;
    }

    public long getSrchuv() {
        return srchuv;
    }

    public void setSrchuv(long srchuv) {
        this.srchuv = srchuv;
    }

    public long getClkuv() {
        return clkuv;
    }

    public void setClkuv(long clkuv) {
        this.clkuv = clkuv;
    }

    public long getTransuv() {
        return transuv;
    }

    public void setTransuv(long transuv) {
        this.transuv = transuv;
    }

    public long getDirectTrans() {
        return directTrans;
    }

    public void setDirectTrans(long directTrans) {
        this.directTrans = directTrans;
    }

    public long getIndirectTrans() {
        return indirectTrans;
    }

    public void setIndirectTrans(long indirectTrans) {
        this.indirectTrans = indirectTrans;
    }

    public long getHolmesClks() {
        return holmesClks;
    }

    public void setHolmesClks(long holmesClks) {
        this.holmesClks = holmesClks;
    }

    public long getArrivalCnt() {
        return arrivalCnt;
    }

    public void setArrivalCnt(long arrivalCnt) {
        this.arrivalCnt = arrivalCnt;
    }

    public long getEffectArrCnt() {
        return effectArrCnt;
    }

    public void setEffectArrCnt(long effectArrCnt) {
        this.effectArrCnt = effectArrCnt;
    }

    public long getHopCnt() {
        return hopCnt;
    }

    public void setHopCnt(long hopCnt) {
        this.hopCnt = hopCnt;
    }

    public int getResTime() {
        return resTime;
    }

    public void setResTime(int resTime) {
        this.resTime = resTime;
    }

    public BigDecimal getArrivalRate() {
        return arrivalRate;
    }

    public void setArrivalRate(BigDecimal arrivalRate) {
        this.arrivalRate = arrivalRate;
    }

    public BigDecimal getHopRate() {
        return hopRate;
    }

    public void setHopRate(BigDecimal hopRate) {
        this.hopRate = hopRate;
    }

    public BigDecimal getAvgResTime() {
        return avgResTime;
    }

    public void setAvgResTime(BigDecimal avgResTime) {
        this.avgResTime = avgResTime;
    }

    public String getResTimeStr() {
        return resTimeStr;
    }

    public void setResTimeStr(String resTimeStr) {
        this.resTimeStr = resTimeStr;
    }

    public BigDecimal getSrsur() {
        return srsur;
    }

    public void setSrsur(BigDecimal srsur) {
        this.srsur = srsur;
    }

    public BigDecimal getCusur() {
        return cusur;
    }

    public void setCusur(BigDecimal cusur) {
        this.cusur = cusur;
    }

    public BigDecimal getCocur() {
        return cocur;
    }

    public void setCocur(BigDecimal cocur) {
        this.cocur = cocur;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
