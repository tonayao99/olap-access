package com.baidu.unbiz.olap.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * OLAP查询Mapper类,返回list<Map>格式
 * 
 * @author wangchongjie
 * @fileName OlapMapTypeRowMapper.java
 * @dateTime 2014-7-9 下午6:09:04
 */
@SuppressWarnings("rawtypes")
public class OlapOneColumnRowMapper<T> implements RowMapper {

    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object obj = rs.getObject(1);
        if (obj != null) {
            return (T) obj;
        }
        return null;
    }
}