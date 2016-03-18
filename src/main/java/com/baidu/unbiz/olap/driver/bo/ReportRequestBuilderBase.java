package com.baidu.unbiz.olap.driver.bo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.baidu.unbiz.olap.obj.BaseItem;

public abstract class ReportRequestBuilderBase<T extends BaseItem> {
    protected Class<T> entityClass;

    /**
     * 获取Item类型
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    @SuppressWarnings({ "rawtypes" })
    public Class getItemClazz() {
        return this.entityClass;
    }

    /**
     * 构造方法
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ReportRequestBuilderBase() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (!params[0].toString().equals("T")) {
            entityClass = (Class) params[0];
        }
    }
}
