package com.baidu.unbiz.olap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Olap多表merge的key字段,多表查询时使用, 若未配置，程序默认按多表keys的交集merge
 * 
 * @author wangchongjie
 * @fileName OlapMergeKey.java
 * @dateTime 2014-8-26 上午10:27:27
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OlapMergeKey {

}
