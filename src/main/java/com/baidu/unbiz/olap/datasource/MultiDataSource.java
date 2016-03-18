package com.baidu.unbiz.olap.datasource;

import org.slf4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.baidu.unbiz.olap.common.ThreadContext;
import com.baidu.unbiz.olap.log.AopLogFactory;

/**
 * 多数据源实现类
 * 
 * @author wangchongjie
 * @fileName MultiDataSource.java
 * @dateTime 2015-7-15 下午3:44:21
 */
public class MultiDataSource extends AbstractRoutingDataSource {

    private static final Logger LOG = AopLogFactory.getLogger(MultiDataSource.class);
    
    static {
        ThreadContext.putContext("Bootstrap", Boolean.TRUE);
    }

    private MultiDataSourceKeyContext multiDataSourceKeyContext;

    @Override
    protected Object determineCurrentLookupKey() {
        String key = "";
        try {
            key = multiDataSourceKeyContext.getKey();
        } catch (Throwable e) {
            logTrace(e);
        }
        return key;
    }

    private void logTrace(Throwable e) {

        StackTraceElement[] stackTraces = e.getStackTrace();
        if (Boolean.TRUE.equals(ThreadContext.getContext("Bootstrap"))) {
            // 过滤掉启动的时候，初始化的时候的一次连接没有key，如果有bean去连库数目大于1的库，就需要打印log
            for (int i = 4; i < stackTraces.length; i++) {
                String infos = stackTraces[i].getClassName();
                if (infos.contains("com.baidu.unbiz")) {
                    LOG.error("get data source key fail,will use default data source", e);
                    break;
                }
            }
        } else { // 不是启动的时候报的找不到key，直接打印log和堆栈
            LOG.error("get data source key fail,will use default data source", e);
            return;
        }
        return;
    }

    /**
     * 获取MultiDataSourceKeyContext
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public MultiDataSourceKeyContext getMultiDataSourceKeyContext() {
        return multiDataSourceKeyContext;
    }

    /**
     * 设置MultiDataSourceKeyContext
     * 
     * @param multiDataSourceKeyContext
     * @since 2015-7-28 by wangchongjie
     */
    public void setMultiDataSourceKeyContext(MultiDataSourceKeyContext multiDataSourceKeyContext) {
        this.multiDataSourceKeyContext = multiDataSourceKeyContext;
    }

}
