package com.baidu.unbiz.olap.router;

import org.slf4j.Logger;
import org.springframework.context.support.ApplicationObjectSupport;

import com.baidu.unbiz.olap.datasource.VirtualDataSource;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.rule.DBShardingRule;

/**
 * Olap ID路由器
 * 
 * @author wangchongjie
 * @fileName IDRouter.java
 * @dateTime 2015-7-15 下午7:12:04
 */
public class IDRouter extends ApplicationObjectSupport implements Router {

    private static final Logger LOG = AopLogFactory.getLogger(IDRouter.class);
    private String prefix;
    private DBShardingRule dbShardingRule;

    /**
     * 设置前缀
     * 
     * @param prefix
     * @since 2015-7-28 by wangchongjie
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 设置DBShardingRule
     * 
     * @param dbShardingRule
     * @since 2015-7-28 by wangchongjie
     */
    public void setDbShardingRule(DBShardingRule dbShardingRule) {
        this.dbShardingRule = dbShardingRule;
    }

    /**
     * 设置目标查询数据源key
     */
    public String getTargetDataSourceKey(Number userId, boolean readMaster) {
        String vdbkey = prefix;
        String dbkey = null;
        String dbCode = null;
        // userid=0的情况放到下一层处理,需要支持返回dbCode为null的情况,当sharding num为1的时候，需要返回null
        dbCode = dbShardingRule.calculateDatabaseNo(userId);
        if (dbCode != null) {
            vdbkey = new StringBuilder(prefix).append("_").append(dbCode).toString();
        }

        VirtualDataSource vdb = (VirtualDataSource) getApplicationContext().getBean(vdbkey, VirtualDataSource.class);

        if (vdb != null) {
            dbkey = vdb.getDataSourceKey(readMaster);
            return dbkey;
        } else {
            String message = "Can't find olap dbkey mapping to user" + userId;
            RuntimeException t = new IllegalArgumentException(message);
            LOG.error(message, t);
            throw t;
        }
    }

}
