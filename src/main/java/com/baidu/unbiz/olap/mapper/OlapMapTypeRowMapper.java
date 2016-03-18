package com.baidu.unbiz.olap.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * OLAP查询Mapper类,返回list<Map>格式
 * 
 * @author wangchongjie
 * @fileName OlapMapTypeRowMapper.java
 * @dateTime 2014-7-9 下午6:09:04
 */
@SuppressWarnings("rawtypes")
public class OlapMapTypeRowMapper implements RowMapper {

    @Override
    public Map<String, BigInteger> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, BigInteger> olapMap = new HashMap<String, BigInteger>();

        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            // String column = meta.getColumnName(i);
            String column = meta.getColumnLabel(i);
            BigDecimal num = rs.getBigDecimal(column);
            if (num != null) {
                olapMap.put(column, num.toBigInteger());
            }
        }
        return olapMap;
    }
}