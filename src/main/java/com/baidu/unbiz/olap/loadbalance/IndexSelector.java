package com.baidu.unbiz.olap.loadbalance;

/**
 * 索引查询器接口
 * 
 * @author wangchongjie
 * @fileName IndexSelector.java
 * @dateTime 2014-9-15 下午5:43:45
 */
public interface IndexSelector {

    /**
     * 查询生效索引
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    int select();

    /**
     * 初始化大小
     * 
     * @param size
     * @since 2015-7-28 by wangchongjie
     */
    void initSize(int size);
}
