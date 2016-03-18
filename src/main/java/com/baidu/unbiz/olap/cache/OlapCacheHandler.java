package com.baidu.unbiz.olap.cache;

import java.util.List;
import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * Olap缓存处理器
 * 
 * @author wangchongjie
 * @fileName OlapCacheHandler.java
 * @dateTime 2014-9-17 下午2:21:43
 */
public interface OlapCacheHandler {

    public static final String COUNT_PREFIX = "COUNT";
    public static final String QUERY_PREFIX = "QUERY";

    /**
     * 从缓存中查询数据
     * 
     * @param key
     * @return 缓存对象
     * @since 2015-7-28 by wangchongjie
     */
    public List<? extends ItemAble> queryFromCache(String key);

    /**
     * 将数据进行缓存
     * 
     * @param key
     * @param values
     * @since 2015-7-28 by wangchongjie
     */
    public void putIntoCache(String key, List<? extends ItemAble> values);

    /**
     * 从缓存中查询数据条数
     * 
     * @param key
     * @return 数据条数
     * @since 2015-7-28 by wangchongjie
     */
    public Integer countFromCache(String key);

    /**
     * 将数据条数进行缓存
     * 
     * @param key
     * @param count
     * @since 2015-7-28 by wangchongjie
     */
    public void putCountIntoCache(String key, Integer count);
}
