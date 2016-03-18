package com.baidu.unbiz.olap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Olap表相关信息
 * 
 * @author wangchongjie
 * @fileName OlapTable.java
 * @dateTime 2014-1-4 下午8:15:21
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OlapTable {

    /**
     * 表名
     */
    String name();

    /**
     * 需聚合的key列
     */
    String[] keyVal() default {};

    /**
     * 基础数据列
     */
    String[] basicVal() default {};

    /**
     * 扩展列名
     */
    String[] extCol() default {};

    /**
     * 扩展列表达式
     */
    String[] extExpr() default {};

    /**
     * 过滤表达式
     */
    String[] filter() default {};

    /**
     * 应用olap列别名的原始列名
     */
    String[] aliasCol() default {};
}
