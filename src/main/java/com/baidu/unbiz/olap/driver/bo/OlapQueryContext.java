package com.baidu.unbiz.olap.driver.bo;

/**
 * Olap Query请求上下文环境
 * 
 * @author wangchongjie
 * @fileName OlapContext.java
 * @dateTime 2014-10-8 下午2:31:31
 */
public class OlapQueryContext extends OlapContext {

    /**
     * 构造方法
     * 
     * @param request
     */
    public OlapQueryContext(OlapRequest<?> request) {
        super.init(request);
    }

}
