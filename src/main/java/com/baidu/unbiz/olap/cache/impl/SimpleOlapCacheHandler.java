package com.baidu.unbiz.olap.cache.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.baidu.unbiz.olap.cache.OlapCacheHandler;
import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * 本地缓存版本的handler，支持类LRU时间淘汰 注意：会占用系统本机内存，
 * 用户可自行实现NoSql的版的集中式缓存handler
 * 
 * @author wangchongjie
 * @fileName SimpleOlapCacheHandler.java
 * @dateTime 2014-8-20 下午12:07:24
 */
public class SimpleOlapCacheHandler implements OlapCacheHandler {

    private static volatile OlapCacheHandler INSTANCE;
    private static SimpleCountCacheHandler COUNT_HANDLER;

    public static final int EXPIRE_TIME_HOUR = 3; // N小时后数据失效

    private static final Map<String, List<? extends ItemAble>> MASTER_LOCAL_CACHE =
            new ConcurrentHashMap<String, List<? extends ItemAble>>();

    private static final Map<String, List<? extends ItemAble>> BACKUP_LOCAL_CACHE =
            new ConcurrentHashMap<String, List<? extends ItemAble>>();

    private static final AtomicBoolean USE_MASTER_CACHE = new AtomicBoolean(true);

    private SimpleOlapCacheHandler() {
    }

    private boolean shouleUseMaseterCahce() {
        long curHour = System.currentTimeMillis() / (1000 * 60 * 60);
        return curHour / (EXPIRE_TIME_HOUR) % 2 == 0;
    }

    @Override
    public List<? extends ItemAble> queryFromCache(String key) {
        List<? extends ItemAble> result = null;
        Map<String, List<? extends ItemAble>> usedCache = MASTER_LOCAL_CACHE;
        Map<String, List<? extends ItemAble>> noUsedCache = BACKUP_LOCAL_CACHE;
        
        if (!shouleUseMaseterCahce()) {
            usedCache = BACKUP_LOCAL_CACHE;
            noUsedCache = MASTER_LOCAL_CACHE;
        } 
        result = usedCache.get(key);
        if (result == null) {
            result = noUsedCache.get(key);
            if (result != null) {
                usedCache.put(key, result);
                noUsedCache.remove(key);
            }
        }
        return result;
    }

    @Override
    public void putIntoCache(String key, List<? extends ItemAble> values) {
        boolean curUseMasterCache = shouleUseMaseterCahce();
        boolean preUseMasterCache = USE_MASTER_CACHE.getAndSet(curUseMasterCache);

        if (curUseMasterCache != preUseMasterCache) {
            if (USE_MASTER_CACHE.get()) {
                BACKUP_LOCAL_CACHE.clear();
            } else {
                MASTER_LOCAL_CACHE.clear();
            }
        }

        if (USE_MASTER_CACHE.get()) {
            MASTER_LOCAL_CACHE.put(key, values);
        } else {
            BACKUP_LOCAL_CACHE.put(key, values);
        }
    }

    public static OlapCacheHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (SimpleOlapCacheHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SimpleOlapCacheHandler();
                    COUNT_HANDLER = new SimpleCountCacheHandler();
                }
            }

        }
        return INSTANCE;
    }

    @Override
    public Integer countFromCache(String key) {
        return COUNT_HANDLER.countFromCache(key);
    }

    @Override
    public void putCountIntoCache(String key, Integer count) {
        COUNT_HANDLER.putCountIntoCache(key, count);
    }
}
