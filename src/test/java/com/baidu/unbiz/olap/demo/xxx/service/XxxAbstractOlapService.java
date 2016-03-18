package com.baidu.unbiz.olap.demo.xxx.service;

import javax.annotation.Resource;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.service.AbstractOlapService;

/**
 * 包装一层，用于处理前后端字段映射等
 * 
 * @author wangchongjie
 * @fileName XxxAbstractOlapService.java
 * @dateTime 2014-8-22 上午11:36:09
 */
public abstract class XxxAbstractOlapService extends AbstractOlapService {

    @Resource(name = "xxxOlapInstanceConfig")
    protected OlapInstanceConfig olapConfig;

    protected OlapInstanceConfig config() {
        return olapConfig;
    }

    @Override
    protected <T> Integer convertOlapTimeUnit(T frontTimeUnit) {
        // return ReportConstants.getOlapTimeUnit((Integer)frontTimeUnit);
        return (Integer) frontTimeUnit;
    }

    @Override
    protected String convertOrderByColumn(String frontColumn) {
        // return ReportConstants.getOlapBackOrderBy(frontColumn);
        return frontColumn;
    }

    // hook for cache wrapper
    // @Resource
    // protected OlapCacheWrapper olapCacheWrapper;
    // @Override
    // protected OlapCacheWrapper getOlapCacheWrapper(){
    // return this.olapCacheWrapper;
    // }

    // hook for cache handler
    // @Override
    // protected OlapCacheHandler getOlapCacheHandler(){
    // return new XxxCacheHandler();
    // }
}
