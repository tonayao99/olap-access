package com.baidu.unbiz.olap.driver.bo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * 报表Olap查询基类
 * 
 * @author wangchongjie
 * @fileName BaseRequest.java
 * @dateTime 2015-7-28 下午4:54:05
 */
public abstract class BaseRequest<T extends ItemAble> {

    protected Class<T> entityClass;

    /**
     * 构造方法
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BaseRequest() {
        if (entityClass == null) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (!params[0].toString().equals("T")) {
                entityClass = (Class) params[0];
            }
        }
    }

    /**
     * 构造方法
     * 
     * @param itemClazz
     */
    public BaseRequest(Class<T> itemClazz) {
        this.entityClass = itemClazz;
    }

    /**
     * 获取Item的类型
     * 
     * @return Item的类型
     * @since 2015-7-28 by wangchongjie
     */
    public Class<? extends ItemAble> getItemClazz() {
        return this.entityClass;
    }

    /**
     * 设置Item的类型
     * 
     * @param clazz
     * @since 2015-7-28 by wangchongjie
     */
    public void setItemClazz(Class<T> clazz) {
        entityClass = clazz;
    }
}
