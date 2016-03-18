package com.baidu.unbiz.olap.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baidu.unbiz.olap.util.DateUtils;
import com.baidu.unbiz.olap.util.Fs64Utils;
import com.baidu.unbiz.olap.util.StringUtils;

/**
 * 一组Olap实例的业务配置
 * 
 * @author wangchongjie
 * @fileName OlapInstanceConfig.java
 * @dateTime 2015-7-16 下午1:33:47
 */
public abstract class OlapInstanceConfig {

    /**
     * Olap实例的userId列的名称
     * 
     * @return Olap实例的userId列的名称
     * @since 2015-7-28 by wangchongjie
     */
    public abstract String userId();

    /**
     * Olap实例的数据库逻辑库名称
     * 
     * @return Olap实例的数据库逻辑库名称
     * @since 2015-7-28 by wangchongjie
     */
    public abstract String database();

    /**
     * 索引表的名称
     * 
     * @return 索引表的名称
     * @since 2015-7-28 by wangchongjie
     */
    public abstract String indexTable();

    /**
     * 该Olap实例的分库数
     * 
     * @return 该Olap实例的分库数
     * @since 2015-7-28 by wangchongjie
     */
    public abstract int shardingNum();

    /**
     * Olap中有数据的开始年
     * 
     * @return Olap中有数据的开始年
     * @since 2015-7-28 by wangchongjie
     */
    public abstract int startYear();

    /**
     * Olap中有数据的开始月
     * 
     * @return Olap中有数据的开始月
     * @since 2015-7-28 by wangchongjie
     */
    public abstract int startMonth();

    /**
     * Olap中有数据的开始天
     * 
     * @return Olap中有数据的开始天
     * @since 2015-7-28 by wangchongjie
     */
    public abstract int startDay();

    /**
     * Olap中各个表的数据起始可查询时间
     * 
     * @return Olap中各个表的数据起始可查询时间
     * @since 2015-7-28 by wangchongjie
     */
    public abstract String olapTableStartDateDictConf();

    /**
     * dbShardingRule的bean名称
     * 
     * @return dbShardingRule的bean名称
     * @since 2015-7-28 by wangchongjie
     */
    public abstract String dbShardingRuleBeanName();

    /**
     * olapJdbcTemplate的bean名称
     * 
     * @return olapJdbcTemplate的bean名称
     * @since 2015-7-28 by wangchongjie
     */
    public abstract String olapJdbcTemplateBeanName();
    
    /**
     * Olap或Palo实例的日期列名称
     * @return 日期列名称
     * @since 2015-7-30 by wangchongjie
     */
    public String dateColumn() {
        return "`date`";
    }
    
    /**
     * Olap产品实例指纹
     * 
     * @return  Olap产品实例指纹
     * @since 2015-7-28 by wangchongjie
     */
    public String fingerprint() {
        StringBuilder fingerprint = new StringBuilder().append(userId()).append(database()).append(indexTable())
                .append(shardingNum()).append(startYear()).append(startMonth()).append(startDay())
                .append(olapTableStartDateDictConf()).append(dbShardingRuleBeanName()).append(olapJdbcTemplateBeanName());
        return Fs64Utils.signFs64(fingerprint.toString()).toString();
    }

    private static final String[] PATTERNS = new String[] { "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd" };
    private static volatile Map<String, Date> olapTableStartDateDict;

    /**
     * 获取配置的该olap表的数据起始可查时间
     * 
     * @param tableName
     * @return 该olap表的数据起始可查时间
     * @since 2015-7-28 by wangchongjie
     */
    public Date getTableStartDate(String tableName) {
        if (!StringUtils.isLegalConfValue(this.olapTableStartDateDictConf())) {
            return null;
        }
        if (olapTableStartDateDict == null) {
            synchronized (OlapInstanceConfig.class) {
                if (olapTableStartDateDict == null) {
                    Map<String, Date> tmpMap = new HashMap<String, Date>();

                    for (String item : this.olapTableStartDateDictConf().split(";")) {
                        String[] items = item.trim().split("@");
                        if (items.length == 2) {
                            tmpMap.put(items[0], DateUtils.parseDate(items[1], PATTERNS));
                        }
                    }
                    olapTableStartDateDict = tmpMap;
                }
            }
        }
        return olapTableStartDateDict.get(tableName);
    }

}
