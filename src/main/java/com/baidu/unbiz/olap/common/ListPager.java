package com.baidu.unbiz.olap.common;

import java.util.List;

/**
 * 列表分页器
 * 
 * @author wangchongjie
 * @fileName ListPager.java
 * @dateTime 2015-7-15 下午3:33:01
 */
public class ListPager<T> {
    private int cursor;
    private int size;
    private int step;
    private List<T> list;

    /**
     * 构造方法
     * 
     * @param list 原始列表
     * @param step 每页条数
     */
    public ListPager(List<T> list, int step) {
        this.list = list;
        this.step = step;
        this.size = list.size();
        this.cursor = 0;
    }

    /**
     * 是否有下一页
     * 
     * @return 是否有下一页
     * @since 2015-7-28 by wangchongjie
     */
    public boolean hasNext() {
        return cursor < size;
    }

    /**
     * 返回下一页列表数据
     * 
     * @return 下一页列表数据
     * @since 2015-7-28 by wangchongjie
     */
    public List<T> nextPage() {
        int nextCursor = cursor + step;
        List<T> pageList = list.subList(cursor, Math.min(nextCursor, size));
        cursor = Math.min(nextCursor, size);
        return pageList;
    }
}