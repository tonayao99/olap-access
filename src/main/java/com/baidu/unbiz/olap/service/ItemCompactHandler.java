package com.baidu.unbiz.olap.service;

/**
 * Item list行转列压缩,供Olap Item List行转列使用
 * 
 * @author wangchongjie
 * @fileName ItemMergeHandler.java
 * @dateTime 2014-8-26 下午3:46:15
 */
public interface ItemCompactHandler<T> {

    /**
     * 获取item的联合key字段
     * 
     * @param item
     * @return key
     * @since 2015-7-28 by wangchongjie
     */
    String getItemKey(T item);

    /**
     * compactedItem的value将被汇总到item中
     * 
     * @param item
     * @param compactedItem
     * @since 2015-7-28 by wangchongjie
     */
    void valueCompact(T item, T compactedItem);

    /**
     * 对压缩后的列进行二次加工，如生成扩展列
     * 
     * @param item
     * @since 2015-7-28 by wangchongjie
     */
    void afterCompact(T item);
}
