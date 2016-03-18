package com.baidu.unbiz.olap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Olap表字段与业务使用的字段映射
 * 
 * @author wangchongjie
 * @fileName OlapColumn.java
 * @dateTime 2014-1-3 下午2:37:25
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OlapColumn {

    /**
     * olap 中使用的列名
     * 
     */
    String value();

    /**
     * 缓存别名,默认无需配置 可解决多表建模的同一个vo中,olap列名冲突问题
     * 
     */
    String alias() default "";
}
