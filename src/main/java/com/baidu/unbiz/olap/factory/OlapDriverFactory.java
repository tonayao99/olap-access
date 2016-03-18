package com.baidu.unbiz.olap.factory;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;

/**
 * OlapDriver工厂类
 * 
 * @author wangchongjie
 * @fileName OlapDriverFactory.java
 * @dateTime 2015-7-16 下午9:09:19
 */
public interface OlapDriverFactory  {

    /**
     * 根据配置生成一个OlapEngineDriver
     * 
     * @param olapConfig
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    OlapEngineDriver newOlapEngineDriver(OlapInstanceConfig olapConfig);
}
