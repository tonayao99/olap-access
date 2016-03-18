package com.baidu.unbiz.olap.loadbalance;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.olap.constant.LoadBalanceCode;
import com.baidu.unbiz.olap.log.AopLogFactory;


/**
 * 索引选择器工厂方法
 * 
 * @author wangchongjie
 * @fileName IndexSelectorFactory.java
 * @dateTime 2014-9-16 上午10:23:51
 */
@Component
public class IndexSelectorFactory {

    private static Logger log = AopLogFactory.getLogger(IndexSelectorFactory.class);

    /**
     * 构造一个索引查询器
     * 
     * @param loadbalance
     * @return IndexSelector
     * @since 2015-7-28 by wangchongjie
     */
    public IndexSelector newSelector(int loadbalance) {
        if (loadbalance == LoadBalanceCode.RANDOM) {
            log.debug("use random datasource.");
            return new RamdomIndexSelector();

        } else if (loadbalance == LoadBalanceCode.ROUND_ROBIN) {
            log.debug("user polling datasource.");
            return new RoundRobinIndexSelector();

        } else if (loadbalance == LoadBalanceCode.MASTER_BACKUP) {
            log.debug("user master-backup datasource.");
            return new MasterBackupIndexSelector();
        }
        return null;
    }

    /**
     * 构造一个索引查询器
     * 
     * @param loadbalance
     * @param size 容量大小
     * @return IndexSelector
     * @since 2015-7-28 by wangchongjie
     */
    public IndexSelector newSelector(int loadbalance, int size) {
        if (loadbalance == LoadBalanceCode.RANDOM) {
            log.debug("use random datasource.");
            return new RamdomIndexSelector(size);

        } else if (loadbalance == LoadBalanceCode.ROUND_ROBIN) {
            log.debug("user polling datasource.");
            return new RoundRobinIndexSelector(size);

        } else if (loadbalance == LoadBalanceCode.MASTER_BACKUP) {
            log.debug("user master-backup datasource.");
            return new MasterBackupIndexSelector(size);
        }
        return null;
    }
}
