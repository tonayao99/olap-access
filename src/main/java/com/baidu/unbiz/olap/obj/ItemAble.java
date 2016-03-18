package com.baidu.unbiz.olap.obj;

import java.io.Serializable;

/**
 * Olap领域模型建模需实现的接口
 * 
 * @author wangchongjie
 * @fileName OlapAble.java
 * @dateTime 2015-7-28 下午2:34:03
 */
public interface ItemAble extends Serializable {
    
    /**
     * Item组装完后的回调方法，运行且只运行一次
     * 
     * @param timeUnit
     * @since 2015-7-28 by wangchongjie
     */
    void afterAssemble(int timeUnit);
    
    /**
     * Item多表merge完后的回调方法，运行且只运行一次
     * 
     * @since 2015-7-28 by wangchongjie
     */
    void afterMultiTableQueryAssemble();

}
