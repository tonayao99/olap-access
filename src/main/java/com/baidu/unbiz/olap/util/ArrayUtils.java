package com.baidu.unbiz.olap.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
    /**
     * 判断数组是否为空
     * 
     * @param array
     * @return 2013-12-10 下午4:56:29 created by wangchongjie
     */
    public static <T> boolean isArrayEmpty(T[] array) {
        if (null == array || array.length <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据是否非空
     * 
     * @param array
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> boolean isArrayNotEmpty(T[] array) {
        return !isArrayEmpty(array);
    }

    /**
     * 数组转列表
     * 
     * @param array
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> List<T> arrayToList(T[] array) {
        return isArrayEmpty(array) ? new ArrayList<T>(1) : Arrays.asList(array);
    }
}
