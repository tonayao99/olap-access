package com.baidu.unbiz.olap.driver;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.baidu.unbiz.olap.driver.bo.OlapRequest;
import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * OLAP数据查询DAO层Driver封装
 * 
 * @author wangchongjie
 * @fileName OlapStatDaoImpl.java
 * @dateTime 2013-12-4 下午1:18:55
 */
public interface OlapEngineDriver extends OlapEngineDriverSupport {

    /**
     * 从olap中查询所需item列表
     * 
     * @param request
     * @return 结果列表
     * @since 2015-7-28 by wangchongjie
     */
    <T extends ItemAble> List<T> getStorageData(OlapRequest<T> request);

    /**
     * 从olap中查询所需String类型item列表
     * 
     * @param request
     * @return String类型item列表
     * @since 2015-7-28 by wangchongjie
     */
    List<String> getStrStorageData(OlapRequest<?> request);

    /**
     * 获取olap表符合条件的记录数
     * 
     * @param request
     * @return 条数
     * @since 2015-7-28 by wangchongjie
     */
    <T extends ItemAble> int countStorageData(OlapRequest<T> request);

    /**
     * 通过SQL查询数据
     * 若为多用户查询，则user需在同一个sharding上，userId传任意一个
     * 
     * @param sql
     * @param userId
     * @return List<Map>
     * @since 2015-7-28 by wangchongjie
     */
    <N extends Number> List<Map<String, BigInteger>> getStorageDataBySQL(String sql, N userId);

    /**
     * 通过SQL查询数据,带参数
     * 若为多用户查询，则user需在同一个sharding上，userId传任意一个
     * 
     * @param sql
     * @param params
     * @param userId
     * @return List<Map>
     * @since 2015-7-28 by wangchongjie
     */
    <N extends Number, O extends Object> List<Map<String, BigInteger>> getStorageDataBySQL(String sql, List<O> params,
            N userId);

    /**
     * 通过OlapRequest查询
     * list<Map>格式返回olap查询结果
     * 
     * @param request
     * @return List<Map>
     * @since 2015-7-28 by wangchongjie
     */
    @SuppressWarnings("rawtypes")
    List<Map<String, BigInteger>> getStorageDataByRequest(OlapRequest request);

    /**
     * 在一个随机的shard上执行sql语句
     * 
     * @param sql
     * @return List<Map>
     * @since 2015-7-28 by wangchongjie
     */
    List<Map<String, Object>> executeSQLonOneRandomShard(String sql);
    
    /**
     * 在一个确定的shard上执行sql语句
     * 
     * @param sql
     * @return
     */
    List<Map<String, Object>> executeSQLonSameShard(String sql);

    /**
     * 在所有sharding上按sql查询一列
     * 
     * @param sql
     * @return 该列数据的列表
     * @since 2015-7-28 by wangchongjie
     */
    <T> List<T> getOneColumnBySqlOnAllShard(String sql);

    /**
     * 获取所有分片中指定时间内有消费的userIds 
     * 优先使用参数indexTable，其次为配置的INDEXTABLE，再其次为request中的第一个表
     * 
     * @param request
     * @param indexTable
     * @return 所有不重复的userid的集合
     * @since 2015-7-28 by wangchongjie
     */
    <N, T extends ItemAble> List<N> getAllUniqUserIds(OlapRequest<T> request, String indexTable);
}