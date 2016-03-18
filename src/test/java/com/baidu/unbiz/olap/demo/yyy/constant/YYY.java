package com.baidu.unbiz.olap.demo.yyy.constant;


/**
 * OlapEngine中字段常量
 * 该类应由olap-access模块的调用者实现
 * 
 * @author wangchongjie
 * @fileName OlapConstants.java
 * @dateTime 2014-1-3 下午2:59:19
 */
public class YYY {
	
	//列名
	public static final class COLUMN{
		// 特殊字段
		public static final String USERID = "DspId";				//olap主key id
		// 数据字段
		public static final String SRCHS = "Search";				//展现
		public static final String CLKS = "Click";					//点击
		public static final String COST = "Cost";					//点击价值
		public static final String CTR = "ctr";						//点击率
		public static final String ACP = "acp";						//平均点击价格
		public static final String CPM = "cpm";						//千次展现价值
		// 分网站报表使用
		public static final String SITEURL = "siteurl";			    //网站URL
		public static final String MAINSITEID = "MainSiteId";		//主域ID
		public static final String SUBSITEID = "SiteId";			//二级域ID
		// 分尺寸报表使用
		public static final String ADSIZEID = "Adsize";				//adsizeID
		public static final String CREATIVESIZEID = "CreativeSize";//adsizeID
		public static final String DSPID = "DspId";
		
		public static final String CLKSALAIS = "clksAlias";                    //点击
	}
	
	//扩展列表达式
	public static final class EXPR{
		public static final String CTR = COLUMN.CLKS + "/" + COLUMN.SRCHS;
		public static final String ACP = COLUMN.COST + "/" + COLUMN.CLKS + "/100000";		//OlapEngine中单位为千分之一分
		public static final String CPM = COLUMN.COST + "/" + COLUMN.SRCHS + "/100"; 		//OlapEngine中单位为千分之一分,此处 *1000 /1000
	}
	
	//olap表名
	public static final class TABLE{
		public static final String SITE = "DailySiteStats,HourlySiteStats";
		public static final String ADSIZE = "DailyAdSizeStats,HourlyAdSizeStats";
		public static final String ADVERTISER = "DailyAdvertiserStats,HourlyAdvertiserStats";
		public static final String CREATIVE = "DailyCreativeStats,HourlyCreativeStats";
	}

}
