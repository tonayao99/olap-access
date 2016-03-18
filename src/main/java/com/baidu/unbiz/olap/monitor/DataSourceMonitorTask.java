package com.baidu.unbiz.olap.monitor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.jdbc.core.JdbcTemplate;

import com.baidu.unbiz.olap.datasource.VirtualDataSource;
import com.baidu.unbiz.olap.log.AopLogFactory;

public class DataSourceMonitorTask extends ApplicationObjectSupport {

    protected static final Logger LOG = AopLogFactory.getLogger(DataSourceMonitorTask.class);

    @Resource
    private List<VirtualDataSource> dataSourceList;

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    /**
     * 检验数据源是否正常
     * 
     * @since 2015-7-28 by wangchongjie
     */
    public void checkDataSource() {
        for (VirtualDataSource vdb : dataSourceList) {
            String vdbId = vdb.getVdbId();
            List<String> mkeys = vdb.getMasterSet();
            if (CollectionUtils.isNotEmpty(mkeys)) {
                List<String> masterKeys = new ArrayList<String>();
                for (String dbKey : mkeys) {
                    LOG.debug("check datasource:" + dbKey);
                    DataSource ds = getApplicationContext().getBean(dbKey, DataSource.class);
                    if (isConnect(ds)) {
                        masterKeys.add(dbKey);
                    }
                }
                
                if (needChange(vdb.getMasterPools(), masterKeys, vdbId)) {
                    LOG.warn(vdbId + " changed ds master pool:" + vdb.getMasterPools() + "->" + masterKeys);
                    vdb.setMasterPools(masterKeys);
                }
            }

            List<String> skeys = vdb.getSlaveSet();
            if (CollectionUtils.isNotEmpty(skeys)) {
                List<String> slaveKeys = new ArrayList<String>();
                for (String dbKey : skeys) {
                    LOG.debug("check datasource:" + dbKey);
                    DataSource ds = getApplicationContext().getBean(dbKey, DataSource.class);
                    if (isConnect(ds)) {
                        slaveKeys.add(dbKey);
                    }
                }
                if (needChange(vdb.getSlavePools(), slaveKeys, vdbId)) {
                    LOG.warn(vdbId + "changed ds slave pool:" + vdb.getSlavePools() + "->" + slaveKeys);
                    vdb.setSlavePools(slaveKeys);
                }
            }

        }
        LOG.debug("check olap datasource done");
    }

    /**
     * 语义存活
     * 
     * @param dataSource
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private boolean isConnect(DataSource dataSource) {
        jdbcTemplate.setDataSource(dataSource);
        try {
            jdbcTemplate.queryForObject("select now() as nowTime", Date.class);
        } catch (Throwable t) {
            LOG.warn("data source can not connect:", t);
            return false;
        }
        return true;
    }

    /**
     * 数据源需变更
     * 
     * @param usedList
     * @param newList
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private boolean needChange(List<String> usedList, List<String> newList, String vdbId) {
        if (CollectionUtils.isEmpty(newList)) {
            LOG.error(vdbId + " all datasource is out of connection!!!");
            return false;
        }
        if (usedList.size() != newList.size()) {
            return true;
        }
        for (String key : usedList) {
            if (!newList.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public List<VirtualDataSource> getDataSourceList() {
        return dataSourceList;
    }

    public void setDataSourceList(List<VirtualDataSource> dataSourceList) {
        this.dataSourceList = dataSourceList;
    }

}
