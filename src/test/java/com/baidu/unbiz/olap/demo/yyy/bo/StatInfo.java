
package com.baidu.unbiz.olap.demo.yyy.bo;

import java.math.BigDecimal;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;
import com.baidu.unbiz.olap.obj.BaseItem;
import com.baidu.unbiz.olap.util.NumberUtil;


/**
 * @Function: 统计数据基础类
 * @author   <a href="mailto:wangchongjie@baidu.com">wangchongjie</a>
 * @created  2013-5-6
 * @since    开放平台(ADX 0.5)
 */
public class StatInfo extends BaseItem {

	private static final long serialVersionUID = 5262324820760421866L;
	
	//属性名与Constants中对应值相同
	/**
	 * 基础统计数据
	 */
	@OlapColumn(YYY.COLUMN.SRCHS)
	protected long srchs;//展现
	
	@OlapColumn(YYY.COLUMN.CLKS)
	protected long clks;//点击
	
	@OlapColumn(YYY.COLUMN.COST)
	protected double cost;//单位元
	
	@OlapColumn(YYY.COLUMN.CTR)
	protected BigDecimal ctr;//点击率
	
	@OlapColumn(YYY.COLUMN.ACP)
	protected BigDecimal acp;//平均点击价格
	
	@OlapColumn(YYY.COLUMN.CPM)
	protected BigDecimal cpm;//千次展现成本
	
	@Override
	public void afterAssemble(int timeUnit){
		super.afterAssemble(timeUnit);
		this.buildCostField();
	}

	private void buildCostField(){
		//注意：由于对象中存储的cost是元，而record中的是分,且为千次，因此要/1000*100
		this.cost =  this.cost  / (1000 * 100.0d);
	}
	
	public BigDecimal getAcp() {
		return acp;
	}

	public void setAcp(double acp) {
		this.acp = NumberUtil.doubleToBigDecimal(acp, 2);
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

	public BigDecimal getCpm() {
		return cpm;
	}

	public void setCpm(double cpm) {
		this.cpm = NumberUtil.doubleToBigDecimal(cpm, 2);
	}

	public BigDecimal getCtr() {
		return ctr;
	}

	public void setCtr(double ctr) {
		this.ctr = NumberUtil.doubleToBigDecimal(ctr);
	}

	public long getSrchs() {
		return srchs;
	}

	public void setSrchs(long srchs) {
		this.srchs = srchs;
	}

	public void setCtr(BigDecimal ctr) {
		this.ctr = ctr;
	}

	public void setAcp(BigDecimal acp) {
		this.acp = acp;
	}

	public void setCpm(BigDecimal cpm) {
		this.cpm = cpm;
	}

}
