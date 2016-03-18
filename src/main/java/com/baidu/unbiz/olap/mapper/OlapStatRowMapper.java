package com.baidu.unbiz.olap.mapper;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.annotation.OlapTablePlus;
import com.baidu.unbiz.olap.common.MethodPair;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.obj.BaseItem;
import com.baidu.unbiz.olap.util.OlapUtils;

/**
 * OLAP查询Mapper类
 * 
 * @author wangchongjie
 * @fileName OlapStatDaoImpl.java
 * @dateTime 2013-12-10 下午5:18:49
 */
@SuppressWarnings("rawtypes")
public class OlapStatRowMapper<ENTITY extends BaseItem> implements RowMapper {

    private static final Logger LOG = AopLogFactory.getLogger(OlapStatRowMapper.class);

    /**
     * OlapEngine返回的实体类型
     */
    private Class<ENTITY> entityClass;

    /**
     * 时间粒度
     */
    private int timeUnit;

    /**
     * mapper的字段关系存储
     */
    protected Map<String, MethodPair> methodMap;

    /**
     * 装在对象时候要执行的setter方法的映射关系，key为column，value为setter方法
     */
    protected List<Map.Entry<String, MethodPair>> entries;

    /**
     * 基础value列名集合
     */
    protected Set<String> basicValSet;
    
    /**
     * 基础别名列集合
     */
    protected Set<String> alaisValSet;

    public OlapStatRowMapper(Class<ENTITY> entityClass, int timeUnit) {
        this.entityClass = entityClass;
        this.timeUnit = timeUnit;
        generateMapper();
    }

    /**
     * 根据反射加载对象的方法。
     * 
     * 工作步骤：
     * 
     * 1，分析实体类。 
     * 解析过程首先找到所有的字段，找到每个字段对应的getter与setter. 
     * 如果字段有annotation则从注释中获取column，否则从getter中取注释， 
     * 依然不存在则从setter中获取注释.
     * 如果注解都不存在，则按照filed的名字作为数据库中的字段名字. 
     * 将数据库字段名，以及对应的setter方法放入缓存，一边装载对象时执行. 
     * 2，根据分析结果利用反射加载对象。 
     * 3，如果数据库中没有对应的字段，则不加载。
     */
    public ENTITY mapRow(ResultSet rs, int rowIndex) throws SQLException {

        try {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = rs.getMetaData().getColumnCount();
            ENTITY entity = entityClass.newInstance();

            boolean allValueEqualZero = true;
            for (int i = 1; i <= columnCount; i++) {
                String column = meta.getColumnLabel(i);
                MethodPair methods = methodMap.get(column);
                methods = (methods == null) ? methodMap.get((column = column.toLowerCase())) : methods;

                if (methods == null) { // H2 Compatible
                    String keyColumn = OlapUtils.getKeyColumnInMethodMapper(methodMap, column, entityClass);
                    methods = methodMap.get(keyColumn);
                }
                if (methods == null) {
                    LOG.error("Error in " + column + " , no setter found in class: " + entityClass.getName());
                    continue;
                }

                Method setter = methods.setter;
                Class<?> paramClass = setter.getParameterTypes()[0];
                Object value = getValue4Type(rs, column, paramClass);

                if (allValueEqualZero
                        && (basicValSet.contains(column.toLowerCase()) || alaisValSet.contains(column.toLowerCase()))
                        && null != value) {
                    BigDecimal a = new BigDecimal(value.toString());
                    if (a.intValue() != 0) {
                        allValueEqualZero = false;
                    }
                }

                try {
                    if (value != null) {
                        Type[] types = setter.getGenericParameterTypes();
                        if (types[0].equals(BigInteger.class)) {
                            setter.invoke(entity, new BigInteger(value.toString()));
                        } else {
                            setter.invoke(entity, value);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Error in " + setter.getName() + " invoke with param :" + value == null ? "null" : value
                            .toString() + " type is " + value == null ? "null" : value.getClass().toString());
                    throw e;
                }
            }
            // 构造其它成员变量
            Method afterAssembleMethod = OlapUtils.getAfterAssembleMethod(entityClass);
            afterAssembleMethod.invoke(entity, this.timeUnit);

            if (allValueEqualZero) {
                return null;
            } else {
                return entity;
            }
        } catch (Exception e) {
            LOG.error("loadEntity Fail: ", e);
            throw new SQLException("error in loadEntity", e);
        }
    }

    /**
     * 生成映射方法
     */
    protected void generateMapper() {

        methodMap = OlapUtils.generateMethodMapper(entityClass);
        entries = new ArrayList<Map.Entry<String, MethodPair>>(methodMap.size());
        entries.addAll(methodMap.entrySet());

        OlapTable mainTable = entityClass.getAnnotation(OlapTable.class);
        String[] basicVals = mainTable.basicVal();
        this.basicValSet = new HashSet<String>();
        for (String val : basicVals) {
            this.basicValSet.add(val.toLowerCase());
        }

        OlapTablePlus tablePlus = entityClass.getAnnotation(OlapTablePlus.class);
        if (tablePlus != null) {
            OlapTable[] subTables = tablePlus.value();
            for (OlapTable subTable : subTables) {
                basicVals = subTable.basicVal();
                for (String val : basicVals) {
                    basicValSet.add(val.toLowerCase());
                }
            }
        }
        this.alaisValSet = new HashSet<String>();
        Set<String> oriAlaisValSet = OlapUtils.getReverseAlaisColumnDict(entityClass).keySet();
        for (String val : oriAlaisValSet) {
            this.alaisValSet.add(val.toLowerCase());
        }
    }

    /**
     * 在rs中获取column字段的typeClass型的值
     */
    protected Object getValue4Type(ResultSet rs, String column, Class<?> typeClass) throws SQLException {

        if (Collection.class.isAssignableFrom(typeClass)) {
            return null;
        }

        try {
            rs.findColumn(column);
        } catch (Exception e) {
            LOG.error("get value fail: ", e);
            return null;
        }

        if (typeClass.equals(Integer.class) || typeClass.equals(Integer.TYPE)) {
            return rs.getInt(column);
        }
        if (typeClass.equals(Long.class) || typeClass.equals(Long.TYPE)) {
            return rs.getLong(column);
        }
        if (typeClass.equals(Boolean.class) || typeClass.equals(Boolean.TYPE)) {
            return rs.getBoolean(column);
        }
        if (typeClass.equals(Float.class) || typeClass.equals(Float.TYPE)) {
            return rs.getFloat(column);
        }
        if (typeClass.equals(Double.class) || typeClass.equals(Double.TYPE)) {
            return rs.getDouble(column);
        }
        if (typeClass.equals(Byte.class) || typeClass.equals(Byte.TYPE)) {
            return rs.getByte(column);
        }
        if (typeClass.equals(String.class)) {
            return rs.getString(column);
        }
        if (Date.class.isAssignableFrom(typeClass)) {
            return rs.getTimestamp(column);
        }
        if (java.sql.Date.class.isAssignableFrom(typeClass)) {
            return rs.getDate(column);
        }

        return rs.getObject(column);
    }

    public List<Map.Entry<String, MethodPair>> getEntries() {
        return entries;
    }
}