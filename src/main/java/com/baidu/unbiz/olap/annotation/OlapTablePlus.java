package com.baidu.unbiz.olap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Olap非主表相关信息
 * 
 * @author wangchongjie
 * @fileName OlapTablePlus.java
 * @dateTime 2014-1-16 上午10:27:52
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OlapTablePlus {

    /**
     * Olap非主表元信息
     * 
     */
    OlapTable[] value();
}
