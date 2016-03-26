package com.baidu.unbiz.olap.cache;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.utils.Fs64Utils;

/**
 * 异步缓存Task
 * 
 * @author wangchongjie
 * @fileName AsynCacheTask.java
 * @dateTime 2014-9-17 下午2:21:05
 */
public class AsynCacheTask implements Runnable {

    protected static final Logger LOG = AopLogFactory.getLogger(AsynCacheTask.class);

    private OlapCacheHandler handler;
    private String key;
    private List<? extends ItemAble> data;
    
    /**
     * 构造方法
     * 
     * @param key
     * @param cacheData
     * @param handler
     */
    public AsynCacheTask(String key, List<? extends ItemAble> cacheData, OlapCacheHandler handler) {
        this.key = key;
        this.data = cacheData;
        this.handler = handler;
    }

    @Override
    public void run() {
        @SuppressWarnings("static-access")
        String cacheKey = Fs64Utils.signFs64(handler.QUERY_PREFIX + key).toString();
        if (StringUtils.isNotEmpty(key) && null != data) {
            handler.putIntoCache(cacheKey, data);
            String format = "AsynCacheTask put key=[{}],cacheKey=[{}] in cache";
            LOG.debug(format, key, cacheKey);
        }
    }

}
