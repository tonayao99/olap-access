package com.baidu.unbiz.olap.factory.impl;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;
import com.baidu.unbiz.olap.factory.OlapDriverFactory;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.rule.DBShardingRule;

@Component
public class OlapDriverFactoryImpl implements OlapDriverFactory, ApplicationContextAware {

    protected static final Logger LOG = AopLogFactory.getLogger(OlapDriverFactoryImpl.class);

    private static ApplicationContext applicationContext; // Spring应用上下文环境
    private static final String OLAPDRIVER_BEANNAME = "olapDriverImpl";

    @Override
    public OlapEngineDriver newOlapEngineDriver(OlapInstanceConfig olapConfig) {
        OlapEngineDriver instance = getSpringBean(OLAPDRIVER_BEANNAME);
        if (instance == null) {
            return null;
        }
        instance.setOlapConfig(olapConfig);
        JdbcTemplate jdbcTemplate = getSpringBean(olapConfig.olapJdbcTemplateBeanName());
        instance.setJdbcTemplate(jdbcTemplate);
        // 可延迟至第一次数据查询时执行
        // instance.initSchemaLoader(instance);
        DBShardingRule dBShardingRule = this.getSpringBean(olapConfig.dbShardingRuleBeanName());
        instance.setDBShardingRule(dBShardingRule);
        // instance.setUserIdDelegateForAllShard();

        return instance;
    }

    /**
     * 获取spring bean
     * 
     * @param beanName
     * @return spring bean
     * @since 2015-7-28 by wangchongjie
     */
    @SuppressWarnings("unchecked")
    private <T> T getSpringBean(String beanName) {
        if (containsBean(beanName)) {
            return (T) getBean(beanName);
        }
        return null;
    }

    private static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    private static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        OlapDriverFactoryImpl.applicationContext = applicationContext;
    }
}
