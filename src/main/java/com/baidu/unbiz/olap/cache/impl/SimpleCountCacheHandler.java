package com.baidu.unbiz.olap.cache.impl;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的olap数据条数缓存处理器，JVM内存不够时清空缓存
 * 
 * @author wangchongjie
 * @fileName SimpleCountCacheHandler.java
 * @dateTime 2014-10-9 下午12:07:51
 */
public class SimpleCountCacheHandler {

    private static volatile SoftReference<Map<String, Integer>> countCacheRef =
            new SoftReference<Map<String, Integer>>(null);

    /**
     * 从缓存中查询数据条数
     * 
     * @param key
     * @return 记录条数
     * @since 2015-7-28 by wangchongjie
     */
    public Integer countFromCache(String key) {
        Map<String, Integer> countCache = countCacheRef.get();
        if (countCache == null) {
            return null;
        }
        return (Integer) countCache.get(key);
    }

    /**
     * 将数据条数进行缓存
     * 
     * @param key
     * @param count
     * @since 2015-7-28 by wangchongjie
     */
    public void putCountIntoCache(String key, Integer count) {
        Map<String, Integer> countCache = countCacheRef.get();
        if (countCache == null) {
            synchronized (SimpleCountCacheHandler.class) {
                if (countCacheRef.get() == null) {
                    countCache = new ConcurrentHashMap<String, Integer>();
                    countCacheRef = new SoftReference<Map<String, Integer>>(countCache);
                }
            }
        }
        countCache.put(key, count);
    }
}
