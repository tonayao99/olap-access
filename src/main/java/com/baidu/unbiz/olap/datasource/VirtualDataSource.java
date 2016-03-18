package com.baidu.unbiz.olap.datasource;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import com.baidu.unbiz.olap.loadbalance.IndexSelector;
import com.baidu.unbiz.olap.loadbalance.IndexSelectorFactory;
import com.baidu.unbiz.olap.log.AopLogFactory;

/**
 * 抽象db数据源
 * 
 * @author wangchongjie
 * @fileName VirtualDataSource.java
 * @dateTime 2014-9-15 下午6:09:45
 */
public class VirtualDataSource implements Serializable, BeanNameAware, InitializingBean {
    private static final long serialVersionUID = 2391107997087589405L;
    private static Logger log = AopLogFactory.getLogger(VirtualDataSource.class);

    @Resource
    protected IndexSelectorFactory factory;

    protected List<String> masterSet;
    protected List<String> slaveSet;

    protected List<String> masterPools;
    protected List<String> slavePools;
    protected int loadbalance = 1; // 1:random 2:polling 3:master-backup

    protected IndexSelector masterSelector;
    protected IndexSelector slaveSelector;

    protected String vdbId;

    /**
     * 获取生效的主库集合
     * 
     * @return MasterSet
     * @since 2015-7-28 by wangchongjie
     */
    public List<String> getMasterSet() {
        return masterSet;
    }

    /**
     * 设置生效的主库集合
     * 
     * @param masterSet
     * @since 2015-7-28 by wangchongjie
     */
    public void setMasterSet(List<String> masterSet) {
        this.masterSet = masterSet;
        this.masterPools = masterSet;
    }

    /**
     * 获取生效的从库集合
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public List<String> getSlaveSet() {
        return slaveSet;
    }

    /**
     * 设置生效的从库集合
     * 
     * @param slaveSet
     * @since 2015-7-28 by wangchongjie
     */
    public void setSlaveSet(List<String> slaveSet) {
        this.slaveSet = slaveSet;
        this.slavePools = slaveSet;
    }

    /**
     * 设置配置的主库池
     * 
     * @param masterPool
     * @since 2015-7-28 by wangchongjie
     */
    public void setMasterPools(List<String> masterPool) {
        this.masterPools = masterPool;
        this.updateSelectorSize();
    }

    /**
     * 获取设置的主库池
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public List<String> getMasterPools() {
        return masterPools;
    }

    /**
     * 设置设置的从库池
     * 
     * @param slavePools
     * @since 2015-7-28 by wangchongjie
     */
    public void setSlavePools(List<String> slavePools) {
        this.slavePools = slavePools;
        this.updateSelectorSize();
    }

    /**
     * 获取设置的从库池
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public List<String> getSlavePools() {
        return slavePools;
    }

    /**
     * 负载均衡策略
     * 
     * @param loadbalance
     * @since 2015-7-28 by wangchongjie
     */
    public void setLoadbalance(int loadbalance) {
        this.loadbalance = loadbalance;
        this.initIndexSelector();
    }

    /**
     * 初始化选择器
     * 
     * @since 2015-7-28 by wangchongjie
     */
    public void initIndexSelector() {
        masterSelector = factory.newSelector(loadbalance);
        slaveSelector = factory.newSelector(loadbalance);
        this.updateSelectorSize();
    }

    /**
     * 更新选择器大小
     * 
     * @since 2015-7-28 by wangchongjie
     */
    private void updateSelectorSize() {
        if (masterSelector != null) {
            masterSelector.initSize(masterPools == null ? 0 : masterPools.size());
        }
        if (slaveSelector != null) {
            slaveSelector.initSize(slavePools == null ? 0 : slavePools.size());
        }
    }

    /**
     * 获取数据源的key
     * 
     * @param readMaster
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public String getDataSourceKey(boolean readMaster) {
        if (readMaster) {
            return getMasterDataSourceKey();
        } else {
            return getSlaveDataSourceKey();
        }
    }

    /**
     * 获取主库数据源key
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public String getMasterDataSourceKey() {
        if (masterPools == null) {
            throw new IllegalStateException("master datasource is null,can't execute the write sql");
        }
        int idx = masterSelector.select();
        return masterPools.get(idx);
    }

    /**
     * 获取从库数据源key
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public String getSlaveDataSourceKey() {
        if (slavePools == null || slavePools.size() == 0) {
            log.warn("read null slave datasource!");
            return getMasterDataSourceKey();
        }
        int idx = slaveSelector.select();
        return slavePools.get(idx);
    }

    /**
     * 属性设置完成后校验
     */
    public void afterPropertiesSet() throws Exception {
        if (masterPools == null) {
            throw new IllegalStateException("visualdatasource initial fail,master pool is null!");
        }
    }

    /**
     * 设置虚拟数据源
     */
    public void setBeanName(String name) {
        this.vdbId = name;
    }

    /**
     * 获取虚拟数据源
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public String getVdbId() {
        return this.vdbId;
    }

}
