package com.baidu.unbiz.olap.service;

import java.util.List;

import com.baidu.unbiz.olap.exception.OlapException;

/**
 * 结果处理器
 * 
 * @author wangchongjie
 * @fileName ResultHandler.java
 * @dateTime 2015-7-15 下午7:28:04
 */
public interface ResultHandler<T> {

    /**
     * 分批处理结果数据
     * 
     * @param itemList
     * @throws OlapException
     * @since 2015-7-28 by wangchongjie
     */
    void process(List<T> itemList) throws OlapException;

    /**
     * 处理完后清理资源
     * 
     * @since 2015-7-28 by wangchongjie
     */
    void cleanup();
}