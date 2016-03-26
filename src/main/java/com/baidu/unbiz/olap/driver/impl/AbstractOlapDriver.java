package com.baidu.unbiz.olap.driver.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.baidu.unbiz.olap.codegen.AtomicComputeCache;
import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;
import com.baidu.unbiz.olap.exception.OlapException;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.rule.DBShardingRule;
import com.baidu.unbiz.olap.schema.SchemaLoader;
import com.baidu.unbiz.olap.utils.StringUtils;

/**
 * OlapEngine DB相关抽象类
 * 
 * @author wangchongjie
 * @fileName AbstractOlapEngineDriver.java
 * @dateTime 2014-9-16 下午4:01:13
 */
public abstract class AbstractOlapDriver implements OlapEngineDriver {
    protected static final Logger LOG = AopLogFactory.getLogger(AbstractOlapDriver.class);

    // need inject
    protected JdbcTemplate jdbcTemplate;

    // need inject
    protected SchemaLoader schemaLoader;
    
    // need inject
    protected OlapInstanceConfig olapConfig;
    
    // need inject
    protected DBShardingRule dbShardRule;
    
    // 可延迟至第一次查询时初始化
    private AtomicBoolean schemaLoaderHasInit = new AtomicBoolean(false);

    protected void lazyInit() {
        if (schemaLoaderHasInit.get()) {
            return;
        } else {
            this.initSchemaLoader(this);
            this.setUserIdDelegateForAllShard();
        }
    }

    /**
     * 初始化schemaLoader
     */
    public void initSchemaLoader(OlapEngineDriver instance) {
        this.schemaLoader  = getInternalSchemaLoader(instance);
    }
    
    /**
     * SchemaLoader缓存
     */
    private static AtomicComputeCache<String, SchemaLoader> atomicComputeSchemaLoaderCache =
            new AtomicComputeCache<String, SchemaLoader>();

    /**
     * 实例化schemaLoader
     * 
     * @param instance
     * @return schemaLoader
     * @since 2015-7-28 by wangchongjie
     */
    private static SchemaLoader getInternalSchemaLoader(final OlapEngineDriver instance) {
        OlapInstanceConfig config = instance.config();
        String keyInCache = config.fingerprint();

        SchemaLoader result = atomicComputeSchemaLoaderCache.getComputeResult(keyInCache, new Callable<Object>() {
            @Override
            public SchemaLoader call() {
                SchemaLoader schemaLoader = new SchemaLoader(instance);
                schemaLoader.start();
                return schemaLoader;
            }
        });
        return result;
    }
    
    /**
     * 按照SQL语句查询并装在记录为一个列表
     * 
     * @param sql
     * @param params
     * @param mapper
     * @return List<T>
     * @since 2015-7-28 by wangchongjie
     */
    protected <T extends Object, O extends Object> List<T> findBySQL(String sql, List<O> params, RowMapper<T> mapper) {
        if (sql == null) {
            throw new OlapException("param sql is null!");
        }
        Object[] args = (params == null) ? new Object[0] : params.toArray(new Object[params.size()]); // 构造参数
        LOG.debug(sql);
        return (List<T>) jdbcTemplate.query(sql, args, mapper); // 查询
    }

    /**
     * 按照SQL语句查询并装在记录为一个列表
     * 
     * @param sql
     * @param params
     * @param mapper
     * @return List<Map<String, BigInteger>>
     * @since 2015-7-28 by wangchongjie
     */
    protected <O extends Object> List<Map<String, BigInteger>> findBySQLReturnMap(String sql, List<O> params,
            RowMapper<Map<String, BigInteger>> mapper) {
        if (sql == null) {
            throw new OlapException("param sql is null!");
        }
        Object[] args = (params == null) ? new Object[0] : params.toArray(new Object[params.size()]); // 构造参数
        LOG.debug(sql);
        return jdbcTemplate.query(sql, args, mapper); // 查询
    }

    /**
     * 按照SQL语句通用查询
     * 
     * @param sql
     * @param params
     * @param mapper
     * @return List<Map<String, Object>>
     * @since 2015-7-28 by wangchongjie
     */
    protected <O extends Object> List<Map<String, Object>> findByGeneralSQL(String sql, List<O> params,
            RowMapper<Map<String, Object>> mapper) {
        if (sql == null) {
            throw new OlapException("param sql is null!");
        }
        Object[] args = (params == null) ? new Object[0] : params.toArray(new Object[params.size()]); // 构造参数
        LOG.debug(sql);
        return jdbcTemplate.query(sql, args, mapper); // 查询
    }

    /**
     * 抽取一列信息信息
     * 
     * @param olapResult
     * @param columnName
     * @return List<Object>
     * @since 2015-7-28 by wangchongjie
     */
    protected List<Object> extractOneColumn(List<Map<String, Object>> olapResult, String columnName) {
        if (olapResult == null || StringUtils.isEmpty(columnName)) {
            return null;
        }
        List<Object> result = new ArrayList<Object>();
        for (Map<String, Object> res : olapResult) {
            result.add(res.get(columnName));
        }
        return result;
    }

    /**
     * 抽取第一列
     * 
     * @param olapResult
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    protected List<Object> extractFirstColumn(List<Map<String, Object>> olapResult) {
        if (olapResult == null) {
            return null;
        }
        Set<String> keys = olapResult.get(0).keySet();
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        String column = keys.iterator().next();
        return this.extractOneColumn(olapResult, column);
    }

    /**
     * 抽取一个值
     * 
     * @param olapResult
     * @param columnName
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public Object extractOneValue(List<Map<String, Object>> olapResult, String columnName) {
        List<Object> res = this.extractOneColumn(olapResult, columnName);
        return res == null ? null : res.get(0);
    }

    /**
     * 抽取第一个值
     * 
     * @param olapResult
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public Object extractFirstValue(List<Map<String, Object>> olapResult) {
        List<Object> res = this.extractFirstColumn(olapResult);
        return res == null ? null : res.get(0);
    }
    
    public OlapInstanceConfig config() {
        return olapConfig;
    }
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void setSchemaLoader(SchemaLoader schemaLoader) {
        this.schemaLoader = schemaLoader;
    }
    
    public void setOlapConfig(OlapInstanceConfig olapConfig) {
        this.olapConfig = olapConfig;
    }
    
    @Override
    public SchemaLoader getSchemaLoader() {
        this.lazyInit();
        return schemaLoader;
    }

    @Override
    public DBShardingRule getDBShardingRule() {
        return dbShardRule;
    }

    @Override
    public void setDBShardingRule(DBShardingRule dbShardRule) {
        this.dbShardRule = dbShardRule;
    }
    
    /**
     * 设置各个分片的userid代表
     */
    private void setUserIdDelegateForAllShard() {
        if (schemaLoader.isUserIdDelegateHasInit()) {
            return;
        }
        int shardingNum = olapConfig.shardingNum();
        Set<Number> userIdDelegate = new HashSet<Number>(shardingNum);
        if (shardingNum == 1) {
            userIdDelegate.add(1);
            schemaLoader.changeUserIdDelegate(userIdDelegate, false);
            return;
        }

        Set<String> allDbcode = new HashSet<String>();
        Random r = new Random();
        long candidateUserId;
        int tryNumber = 10000000;
        while (allDbcode.size() < shardingNum) {
            candidateUserId = Math.abs(r.nextLong());
            if (candidateUserId == 0L) {
                continue;
            }
            String dbcode = dbShardRule.calculateDatabaseNo(candidateUserId);
            if (! allDbcode.contains(dbcode)) {
                allDbcode.add(dbcode);
                userIdDelegate.add(candidateUserId);
            }
            tryNumber--;
            if (tryNumber < 0) {
                throw new OlapException("can not find deputy of userId for all olap shard.");
            }
        }
        schemaLoader.changeUserIdDelegate(userIdDelegate, false);
        LOG.info("userIdDelegate: " + userIdDelegate);
        Assert.isTrue(userIdDelegate.size() == shardingNum);
    }
}
