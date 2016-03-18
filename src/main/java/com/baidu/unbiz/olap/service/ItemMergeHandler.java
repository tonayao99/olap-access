package com.baidu.unbiz.olap.service;

/**
 * 供service层merge业务字段使用
 * 
 * @author wangchongjie
 * @fileName ItemMergeHandler.java
 * @dateTime 2014-8-26 下午3:46:15
 */
public interface ItemMergeHandler<T, N> {

    /**
     * 获取item的联合key字段
     * 
     * @param item
     * @return key
     * @since 2015-7-28 by wangchongjie
     */
    String getItemKey(T item);

    /**
     * 获取字典(通常为db中的广告信息)中的联合key字段
     * 
     * @param dict
     * @return key
     * @since 2015-7-28 by wangchongjie
     */
    String getDictKey(N dict);

    /**
     * 将字典信息填充至item中
     * 
     * @param item
     * @param dict
     * @since 2015-7-28 by wangchongjie
     */
    void mergeDictInfoToItem(T item, N dict);
}
