package com.baidu.unbiz.olap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;

/**
 * 一组缺省Olap实例的业务配置，也可自行定制
 * 
 * @author wangchongjie
 * @fileName DefaultOlapInstanceConfig.java
 * @dateTime 2015-7-16 下午1:33:47
 */
@Component
public class DefaultOlapInstanceConfig extends OlapInstanceConfig {

    @Value("${olap.name.userid:UserId}")
    private String userId;

    @Value("${olap.name.database:olap}")
    private String database;

    @Value("${olap.name.indextable:}")
    private String indexTable;

    @Value("${olap.sharding.num:1}")
    private int shardingNum;

    @Value("${olap.start.year:1986}")
    private int startYear;

    @Value("${olap.start.month:6}")
    private int startMonth;

    @Value("${olap.start.day:25}")
    private int startDay;

    @Value("${olap.singel.table.start.date:}")
    private String olapTableStartDateDictConf;

    @Value("${olap.jdbctemplate.beanname:olapJdbcTemplate}")
    private String olapJdbcTemplateBeanName;

    @Value("${olap.dbshardingrule.beanname:olapShardingRule}")
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
