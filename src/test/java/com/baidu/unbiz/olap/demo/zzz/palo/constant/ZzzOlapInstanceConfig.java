package com.baidu.unbiz.olap.demo.zzz.palo.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;

/**
 * ZZZ产品线一组Palo实例的业务配置
 * 
 * @author wangchongjie
 * @fileName ZzzOlapInstanceConfig.java
 * @dateTime 2015-7-30 上午10:22:14
 */
@Component
public class ZzzOlapInstanceConfig extends OlapInstanceConfig {

    @Value("${zzz.olap.name.userid}")
    private String userId;

    @Value("${zzz.olap.name.database}")
    private String database;

    @Value("${zzz.olap.name.indextable}")
    private String indexTable;

    @Value("${zzz.olap.sharding.num}")
    private int shardingNum;

    @Value("${zzz.olap.start.year}")
    private int startYear;

    @Value("${zzz.olap.start.month}")
    private int startMonth;

    @Value("${zzz.olap.start.day}")
    private int startDay;

    @Value("${zzz.olap.singel.table.start.date}")
    private String olapTableStartDateDictConf;

    @Value("${zzz.olap.jdbctemplate.beanname}")
    private String olapJdbcTemplateBeanName;

    @Value("${zzz.olap.dbshardingrule.beanname}")
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
