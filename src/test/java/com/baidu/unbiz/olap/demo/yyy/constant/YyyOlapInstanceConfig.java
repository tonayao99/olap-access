package com.baidu.unbiz.olap.demo.yyy.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;

/**
 * 一组Olap实例的业务配置
 * 
 * @author wangchongjie
 * @fileName BeidouOlapInstanceConfig.java
 * @dateTime 2015-7-16 下午1:33:47
 */
@Component
public class YyyOlapInstanceConfig extends OlapInstanceConfig {

    @Value("${yyy.olap.name.userid}")
    private String userId = "DspId";

    @Value("${yyy.olap.name.database}")
    private String database = "olap";

    @Value("${yyy.olap.name.indextable}")
    private String indexTable;

    @Value("${yyy.olap.sharding.num}")
    private int shardingNum = 1;

    @Value("${yyy.olap.start.year}")
    private int startYear = 2011;

    @Value("${yyy.olap.start.month}")
    private int startMonth = 6;

    @Value("${yyy.olap.start.day}")
    private int startDay = 15;

    @Value("${yyy.olap.singel.table.start.date}")
    private String olapTableStartDateDictConf;

    @Value("${yyy.olap.jdbctemplate.beanname}")
    private String olapJdbcTemplateBeanName;

    @Value("${yyy.olap.dbshardingrule.beanname}")
    private String dbShardingRuleBeanName;

    
    @Override
    public String userId() {
        return userId;
    }

    @Override
    public String database() {
        return database;
    }

    @Override
    public String indexTable() {
        return indexTable;
    }

    @Override
    public int shardingNum() {
        return shardingNum;
    }

    @Override
    public int startYear() {
        return startYear;
    }

    @Override
    public int startMonth() {
        return startMonth;
    }

    @Override
    public int startDay() {
        return startDay;
    }

    @Override
    public String olapTableStartDateDictConf() {
        return olapTableStartDateDictConf;
    }

    @Override
    public String dbShardingRuleBeanName() {
        return dbShardingRuleBeanName;
    }

    @Override
    public String olapJdbcTemplateBeanName() {
        return olapJdbcTemplateBeanName;
    }

}
