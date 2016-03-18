package com.baidu.unbiz.olap.driver.bo;

import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * Olap Query请求上下文环境
 * 
 * @author wangchongjie
 * @fileName OlapContext.java
 * @dateTime 2014-10-8 下午2:31:31
 */
public class OlapCountContext extends OlapContext {

    /**
     * 构造方法
     * @param request
     */
    public OlapCountContext(OlapRequest<? extends ItemAble> request) {
        super.init(request);
    }

}
