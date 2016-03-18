package com.baidu.unbiz.olap.demo.yyy.service;

import javax.annotation.Resource;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.service.AbstractOlapService;

/**
 * YYY产品线对应的OlapEngine实例
 * 
 * @author wangchongjie
 * @fileName YyyAbstractOlapService.java
 * @dateTime 2015-7-17 下午2:08:40
 */
public abstract class YyyAbstractOlapService extends AbstractOlapService {
    
    @Resource(name = "yyyOlapInstanceConfig")
    protected OlapInstanceConfig olapConfig;
	
    protected OlapInstanceConfig config() {
        return olapConfig;
    }
    
	@Override
	protected <T> Integer convertOlapTimeUnit(T frontTimeUnit){
//		return ReportConstants.getOlapTimeUnit((Integer)frontTimeUnit);
		return (Integer) frontTimeUnit;
	}
	
	@Override
	protected String convertOrderByColumn(String frontColumn){
//		return ReportConstants.getOlapBackOrderBy(frontColumn);
		return frontColumn;
	}
	
}
