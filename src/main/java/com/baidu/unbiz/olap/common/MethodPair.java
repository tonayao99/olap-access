package com.baidu.unbiz.olap.common;

import java.lang.reflect.Method;

/**
 * 一个get与set方法的配对
 * 
 * @fileName OlapStatRowMapper.java
 * @dateTime 2014-1-3 下午7:57:29
 */
public class MethodPair {

    /**
     * 构造方法
     * 
     * @param setterMethod
     * @param getterMethod
     */
    public MethodPair(Method setterMethod, Method getterMethod) {
        this.getter = getterMethod;
        this.setter = setterMethod;
    }

    /**
     * getter
     */
    public Method getter;
    
    /**
     * setter
     */
    public Method setter;
}