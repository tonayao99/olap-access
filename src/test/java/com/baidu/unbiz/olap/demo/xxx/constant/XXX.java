package com.baidu.unbiz.olap.demo.xxx.constant;

/**
 * Xxx产品线的OlapEngine中字段常量
 * 该类应由olap-access模块的调用者实现
 * 
 * @author wangchongjie
 * @fileName Xxx.java
 * @dateTime 2015-7-17 下午2:37:45
 */
public class XXX {
	
    // 列名
    public static final class COLUMN {
        // 特殊字段
        public static final String IGNORE = "ignore"; // olap填充时忽略该字段
        public static final String USERID = "UserId"; // olap主key id
        public static final String TIME = "groupTime"; // 聚合时间字段
        // 数据字段
        public static final String SRCHS = "Search"; // 展现
        public static final String CLKS = "Click"; // 点击
        public static final String COST = "Cost"; // 点击价值
        public static final String TAX = "Tax"; // 当日税费值
        public static final String SRCHUV = "SrchUv"; // 展现uv
        public static final String CLKUV = "ClkUv"; // 点击uv
        public static final String TRANSUV = "TransUv"; // 转化uv
        public static final String DIRECTTRANS = "DirectTransCnt"; // 直接转化数
        public static final String INDIRECTTRANS = "IndirectTransCnt"; // 间接转化数
        public static final String ARRIVALCNT = "ArrivalCnt"; // 到达数
        public static final String HOPCNT = "HopCnt"; // 二跳数
        public static final String RESTIME = "ResTime"; // 停留时间
        public static final String EFFECTARRCNT = "EffectArrCnt"; // 有效到达数
        public static final String ARRIVAL_RATE = "arrivalRate";
        public static final String HOP_RATE = "hopRate";
        public static final String AVG_RES_TIME = "avgResTime";
        public static final String RES_TIME_STR = "resTimeStr";
        public static final String SRSUR = "srsur";
        public static final String CUSUR = "cusur";
        public static final String COCUR = "cocur";

        // 数据扩展列字段
        public static final String CTR = "ctr"; // 点击率
        public static final String ACP = "acp"; // 平均点击价格
        public static final String CPM = "cpm"; // 千次展现价值
        // 主键列字段

        public static final String TRANTSITEID = "TransSiteId"; // 网站URL
        public static final String TRANSTARGETID = "TransTargetId"; // 网站URL
        public static final String SITEURL = "siteurl"; // 网站URL
        public static final String MAINSITEID = "MainSiteId"; // 主域ID
        public static final String SUBSITEID = "SiteId"; // 二级域ID
        public static final String FIRSTTRADEID = "FirstTradeId"; // 一级行业ID
        public static final String SECONDTRADEID = "SecondTradeId"; // 二级行业ID
        public static final String PLANID = "PlanId"; // 计划ID
        public static final String GROUPID = "GroupId"; // 推广组ID
        public static final String UNITID = "CreativeId"; // 创意ID
        public static final String APPID = "AppId"; // APPID
        public static final String GENDERID = "GenderId"; // 性别ID
        public static final String DEVICEID = "DeviceId"; // 设备ID
        public static final String INTERESTID = "InterestId"; // INTERESTID
        public static final String WORDID = "WordId"; // WORDID
        public static final String PRODUCTID = "ProductId"; // 产品ID
        public static final String PATTERNID = "PatternId"; // 模板ID
        public static final String ISPATTERN = "IsPattern"; // 是否模板
        public static final String TAR = "Tar"; // TAR
        public static final String GPID = "GroupPackId"; // 包关联关系ID
        public static final String REFPACKID = "ReferPackId"; // 包引用ID
        public static final String PROVINCEID = "ProvinceId"; // 一级地域ID
        public static final String CITYID = "CityId"; // 二级地域ID
        public static final String ATID = "AtId"; // AtID
        public static final String ATTYPE = "AtType"; // At类型
        public static final String ATTACHID = "AttachId"; // AttachID
        public static final String ATTACHTYPE = "AttachType"; // Attach类型
        public static final String SUBLINKID = "ChainId"; // 分子链ID
        public static final String EXTTAR = "ExtTar"; // 是否扩展流量 0：非扩展流量 非0 扩展流量
        // 别名列
        public static final String HOLMESCLKS = "HolmesClk"; // holmes点击列别名
    }

    // 扩展列表达式
    public static final class EXPR {
        public static final String CTR = COLUMN.CLKS + "/" + COLUMN.SRCHS + "/1";
        public static final String ACP = COLUMN.COST + "/" + COLUMN.CLKS + "/100"; // OlapEngine中单位为分
        public static final String CPM = COLUMN.COST + "/" + COLUMN.SRCHS + "*10/1"; // OlapEngine中单位为分
    }

    // olap表名
    public static final class TABLE {
        // 基础数据单表
        public static final String USER = "DailyUserStats";
        public static final String PLAN = "DailyPlanStats";
        public static final String GROUP = "DailyGroupStats";
        public static final String UNIT = "DailyCreativeStats";
        public static final String SITE = "DailyCreativeSiteStats";
        public static final String TRADE = "DailyTradeStats";
        public static final String APP = "DailyAppStats";
        public static final String INTEREST = "DailyInterestStats";
        public static final String TAX = "DailyTaxStats";
        public static final String GENDER = "DailyGenderStats";
        public static final String DEVICE = "DailyDeviceStats";
        public static final String AT = "DailyAtStats";
        public static final String ATTACH = "DailyAttachStats";
        public static final String SUBLINK = "DailyBeidouChainStats";
        public static final String KEYWORD = "DailyKeywordStats";
        public static final String PACK = "DailyPackStats";
        public static final String REGION = "DailyRegionStats";
        public static final String SI_KEYWORD = "DailySiKeywordStats";
        public static final String SI_PRODUCT = "DailySiProductStats";
        public static final String EXTENTION = "DailyExtentionStats";

        // UV数据单表
        public static final String USER_UV = "DailyUserUvStats";
        public static final String PLAN_UV = "DailyPlanUvStats";
        public static final String GROUP_UV = "DailyGroupUvStats";
        public static final String CREATIVE_UV = "DailyCreativeUvStats";
        public static final String PACK_UV = "DailyPackUvStats";
        public static final String TRADE_UV = "DailyTradeUvStats";
        public static final String KEYWORD_UV = "DailyKeywordUvStats";
        public static final String INTEREST_UV = "DailyInterestUvStats";
        public static final String INTEREST_USER_UV = "DailyInterestUserUvStats";
        public static final String INTEREST_PLAN_UV = "DailyInterestPlanUvStats";
        public static final String INTEREST_GROUP_UV = "DailyInterestGroupUvStats";
        public static final String GENDER_USER_UV = "DailyGenderUserUvStats";
        public static final String GENDER_PLAN_UV = "DailyGenderPlanUvStats";
        public static final String GENDER_UV = "DailyGenderGroupUvStats";
        public static final String REGION_USER_UV = "DailyRegionUserUvStats";
        public static final String REGION_PLAN_UV = "DailyRegionPlanUvStats";
        public static final String REGION_UV = "DailyRegionGroupUvStats";
        public static final String APP_UV = "DailyAppUvStats";
        public static final String AT_UV = "DailyAtUvStats";
        public static final String DEVICE_UV = "DailyDeviceUvStats";
        public static final String EXTENTION_UV = "DailyExtentionUvStats";

        // 转化数据单表
        public static final String GENDER_TRANS = "DailyGenderTransStats";
        public static final String SITE_TRANS = "DailySiteTransStats";
        public static final String PLAN_TRANS = "DailyPlanTransStats";
        public static final String GROUP_TRANS = "DailyGroupTransStats";
        public static final String CREATIVE_TRANS = "DailyCreativeTransStats";
        public static final String TRADE_TRANS = "DailyTradeTransStats";
        public static final String USER_TRANS = "DailyUserTransStats";
        public static final String PACK_TRANS = "DailyPackTransStats";
        public static final String REGION_TRANS = "DailyRegionTransStats";
        public static final String INTEREST_TRANS = "DailyInterestTransStats";
        public static final String KEYWORD_TRANS = "DailyKeywordTransStats";
        public static final String AT_TRANS = "DailyAtTransStats";

        // Holmes数据单表
        public static final String HOLMES = "DailyHolmesStats";

        // 数据复合表,中间件适配
        public static final String USER_PLAN_GROUP_UNIT = USER + "," + PLAN + "," + GROUP + "," + UNIT;
        public static final String USER_PLAN_GROUP_UNIT_UV = USER_UV + "," + PLAN_UV + "," + GROUP_UV + ","
                + CREATIVE_UV;
        public static final String USER_PLAN_GROUP_UNIT_TRANS = USER_TRANS + "," + PLAN_TRANS + "," + GROUP_TRANS + ","
                + CREATIVE_TRANS;
        public static final String USER_PLAN_GROUP_4_REGION_UV = REGION_USER_UV + "," + REGION_PLAN_UV + ","
                + REGION_UV;
        public static final String USER_PLAN_GROUP_4_INTEREST_UV = INTEREST_USER_UV + "," + INTEREST_PLAN_UV + ","
                + INTEREST_GROUP_UV;
        public static final String USER_PLAN_GROUP_4_GENDER_UV = GENDER_USER_UV + "," + GENDER_PLAN_UV + ","
                + GENDER_UV;
        public static final String USER_PLAN_GROUP_4_AUDIENCE_SUM_DATA = USER_UV + "," + PLAN_UV + "," + GROUP_UV;
    }

}
