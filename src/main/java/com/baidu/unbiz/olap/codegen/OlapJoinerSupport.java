package com.baidu.unbiz.olap.codegen;

/**
 * Joiner辅助接口
 * 
 * @author wangchongjie
 * @fileName OlapJoinerSupport.java
 * @dateTime 2015-7-15 下午3:32:20
 */
public interface OlapJoinerSupport {

    /**
     * 获取merge key
     * 
     * @param obj
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    String getKeys(Object obj);

    /**
     * source数据merge到target中
     * 
     * @param source
     * @param target
     * @since 2015-7-28 by wangchongjie
     */
    void setValues(Object source, Object target);
}
