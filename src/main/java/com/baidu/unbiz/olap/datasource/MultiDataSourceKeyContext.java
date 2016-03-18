package com.baidu.unbiz.olap.datasource;

import org.slf4j.Logger;

import com.baidu.unbiz.olap.common.ThreadContext;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.router.Router;

/**
 * 多数据源上下文
 * 
 * @author wangchongjie
 * @fileName MultiDataSourceKeyContext.java
 * @dateTime 2015-7-15 下午3:46:32
 */
public class MultiDataSourceKeyContext {

    private static final Logger LOG = AopLogFactory.getLogger(MultiDataSourceKeyContext.class);

    private String dbName;

    private Router router;

    /**
     * 设置db key
     * 
     * @param userid
     * @param readMaster 是否强制读主库
     * @since 2015-7-28 by wangchongjie
     */
    public void setKey(Integer userid, boolean readMaster) {
        String dbkey = router.getTargetDataSourceKey(userid, readMaster);
        setKey(dbkey);
    }

    /**
     * 设置方法调用时使用的key,存入到threadlocal中
     * 
     * @param key
     * @since 2015-7-28 by wangchongjie
     */
    public void setKey(String key) {
        if (key == null) {
            clearKey();
        } else {
            ThreadContext.putContext(dbName, key);
        }
        LOG.debug("set data source key[" + key + "]");
    }

    /**
     * 获得当前数据源key,如果threadlocal中没有数据源，则返回对应用户的主库连接
     * 
     * @return db key
     * @since 2015-7-28 by wangchongjie
     */
    public String getKey() {
        String dbkey = ThreadContext.getContext(dbName);
        if (dbkey == null) {
            Number userid = ThreadContext.getOlapShardKey();
            if (userid == null) {
                userid = 0;
                // throw new RuntimeException("No userid contexted,so you can't know how to routing!!");
            }
            dbkey = router.getTargetDataSourceKey(userid, true);
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            logTrace(dbkey, elements);
        }
        return dbkey;
    }

    private void logTrace(String dbkey, StackTraceElement[] elements) {
        if (elements == null || elements.length == 0) {
            return;
        }
        for (StackTraceElement ste : elements) {
            if (ste.getClassName().indexOf("service.impl") > 0) {
                LOG.debug("no transaction data source Key[" + dbkey + "," + ste.getClassName() + "."
                        + ste.getMethodName() + ",line number:" + ste.getLineNumber() + "]");
                return;
            }
        }
        for (StackTraceElement ste : elements) {
            LOG.debug("no transaction data source Key[" + dbkey + "," + ste.getClassName() + "." + ste.getMethodName()
                    + ",line number:" + ste.getLineNumber() + "]");
        }
        return;
    }

    /**
     * 获得当前dataSourceContext中实际保存的数据源key,没有就返回null
     */
    public String getActualContextKey() {
        String dbkey = ThreadContext.getContext(dbName);
        LOG.debug("get data source Key[" + dbkey + "]");
        return dbkey;
    }

    /**
     * 清除设置的key
     * 
     * @since 2015-7-28 by wangchongjie
     */
    public void clearKey() {
        ThreadContext.remove(dbName);
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

}
