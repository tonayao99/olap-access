package com.baidu.unbiz.olap.rule;


/**
 * DB路由规则
 * 
 * @author wangchongjie
 * @fileName DBShardingRule.java
 * @dateTime 2015-7-15 下午7:13:54
 */
public interface DBShardingRule {

    /**
     * 根据数据库分库规则，计算数据库的sharding,如果sharding数为1，则返回null
     * 
     * @param userid
     * @return
     */
    String calculateDatabaseNo(Number userid);
    
}
