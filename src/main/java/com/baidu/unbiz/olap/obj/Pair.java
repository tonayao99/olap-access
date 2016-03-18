package com.baidu.unbiz.olap.obj;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 一对通用对象组合
 * 
 * @author wangchongjie
 * @fileName Pair.java
 * @dateTime 2015-7-15 下午7:10:47
 */
public class Pair<T1, T2> {

    public T1 field1;
    public T2 field2;

    public Pair() {

    }

    public Pair(T1 field1, T2 field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    /**
     * 构造一个Pair
     * 
     * @param field1
     * @param field2
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T1, T2> Pair<T1, T2> of(T1 field1, T2 field2) {
        return new Pair<T1, T2>(field1, field2);
    }

    /**
     * 包装一组Pair
     * 
     * @param pairs
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T1, T2> List<Pair<T1, T2>> wrapList(Pair<T1, T2>...pairs) {
        if (pairs == null) {
            return null;
        }
        List<Pair<T1, T2>> result = new ArrayList<Pair<T1, T2>>(pairs.length);
        for (Pair<T1, T2> pair : pairs) {
            result.add(pair);
        }
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
