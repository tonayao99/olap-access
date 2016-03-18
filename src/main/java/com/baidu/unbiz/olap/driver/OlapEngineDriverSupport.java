package com.baidu.unbiz.olap.driver;

import org.springframework.jdbc.core.JdbcTemplate;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.rule.DBShardingRule;
import com.baidu.unbiz.olap.schema.SchemaLoader;

/**
 * OLAP数据查询DAO层Driver辅助类封装
 * 
 * @author wangchongjie
 * @fileName OlapEngineDriverSupport.java
 * @dateTime 2015-7-16 下午5:38:30
 */
public interface OlapEngineDriverSupport {

    // need inject
    /**
     * 初始化SchemaLoader
     * 
     * @param instance
     * @since 2015-7-28 by wangchongjie
     */
    void initSchemaLoader(OlapEngineDriver instance);
    
    // need inject
    /**
     * 设置JdbcTemplate
     * 
     * @param jdbcTemplate
     * @since 2015-7-28 by wangchongjie
     */
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    // need inject
    /**
     * 设置OlapInstanceConfig
     * 
     * @param olapConfig
     * @since 2015-7-28 by wangchongjie
     */
    void setOlapConfig(OlapInstanceConfig olapConfig);

    /**
     * 获取SchemaLoader
     * 
     * @return SchemaLoader
     * @since 2015-7-28 by wangchongjie
     */
    SchemaLoader getSchemaLoader();
    
    /**
     * 获取DBShardingRule
     * 
     * @return DBShardingRule
     * @since 2015-7-28 by wangchongjie
     */
    DBShardingRule getDBShardingRule();
    
    /**
     * 设置DBShardingRule
     * 
     * @param shardRule
     * @since 2015-7-28 by wangchongjie
     */
    void setDBShardingRule(DBShardingRule shardRule);

    /**
     * 获取 OlapInstanceConfig
     * @return OlapInstanceConfig
     * @since 2015-7-28 by wangchongjie
     */
    OlapInstanceConfig config();
    
    /**
     * 设置userid代表，供不指定userid时使用
     * 
     * @since 2015-7-28 by wangchongjie
     */
    // void setUserIdDelegateForAllShard();
}