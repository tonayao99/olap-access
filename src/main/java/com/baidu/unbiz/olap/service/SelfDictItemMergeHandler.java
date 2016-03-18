package com.baidu.unbiz.olap.service;

/**
 * 供service层merge业务字段使用
 * 
 * @author wangchongjie
 * @fileName SelfDictItemMergeHandler.java
 * @dateTime 2014-8-26 下午3:46:15
 */
public abstract class SelfDictItemMergeHandler<T, N> implements ItemMergeHandler<T, N> {

    /**
     * 获取item的联合key字段
     */
    public abstract String getItemKey(T item);

    /**
     * 获取字典(通常为db中的广告信息)中的联合key字段
     */
    public String getDictKey(N dict) {
        return String.valueOf((Object) dict);
    };

    /**
     * 将字典信息填充至item中
     */
    public abstract void mergeDictInfoToItem(T item, N dict);
}
