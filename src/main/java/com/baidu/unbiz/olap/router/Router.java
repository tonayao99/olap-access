package com.baidu.unbiz.olap.router;

/**
 * 路由接口
 * 
 * @author wangchongjie
 * @fileName Router.java
 * @dateTime 2015-7-15 下午7:12:44
 */
public interface Router {

    /**
     * locate target virtualdatasource by routing rule
     * 
     * @param userid
     * @param readMaster
     * @return
     */
    String getTargetDataSourceKey(Number userid, boolean readMaster);

}
