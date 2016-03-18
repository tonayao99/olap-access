package com.baidu.unbiz.olap.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * OLAP查询Mapper类,返回list<String>格式
 * 
 * @author wangchongjie
 * @fileName OlapStringTypeRowMapper.java
 * @dateTime 2015-6-11 下午9:41:06
 */
@SuppressWarnings("rawtypes")
public class OlapStringTypeRowMapper implements RowMapper {

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        StringBuilder result = new StringBuilder();

        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String column = meta.getColumnLabel(i);
            // 默认存储中均为数值类型
            BigDecimal num = rs.getBigDecimal(column);
            result.append(num);
            result.append("\t");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}