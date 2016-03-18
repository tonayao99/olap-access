package com.baidu.unbiz.olap.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * OLAP schema信息查询Mapper类,返回list<Map>格式
 * 
 * @author wangchongjie
 * @fileName GeneralRowMapper.java
 * @dateTime 2014-7-16 下午6:16:32
 */
@SuppressWarnings("rawtypes")
public class GeneralRowMapper implements RowMapper {

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> olapMap = new HashMap<String, Object>();

        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            // String column = meta.getColumnName(i);
            String column = meta.getColumnLabel(i);
            Object obj = rs.getObject(column);
            if (obj != null) {
                olapMap.put(column, obj);
            }
        }
        return olapMap;
    }
}