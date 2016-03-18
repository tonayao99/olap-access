package com.baidu.unbiz.olap.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapMergeKey;
import com.baidu.unbiz.olap.codegen.AtomicComputeCache;
import com.baidu.unbiz.olap.common.MethodPair;
import com.baidu.unbiz.olap.constant.CompareType;
import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.obj.ItemAble;

/**
 * Olap基础工具类
 * 
 * @author wangchongjie
 * @fileName OlapUtils.java
 * @dateTime 2014-1-15 下午3:08:21
 */
public class OlapUtils {

    private static ConcurrentMap<Class<?>, GenerateMethodMapperFutureTask> methodMapperCache =
            new ConcurrentHashMap<Class<?>, GenerateMethodMapperFutureTask>();

    private static class GenerateMethodMapperFutureTask extends FutureTask<Object> {
        public GenerateMethodMapperFutureTask(Callable<Object> callable) {
            super(callable);
        }

        public Object getValue() {
            try {
                return get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static AtomicComputeCache<String, String> atomicComputeKeyColumnCache =
            new AtomicComputeCache<String, String>();

    /**
     * 在methodMap中获取key列
     * 
     * @param methodMap
     * @param column
     * @param clazz
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static String getKeyColumnInMethodMapper(final Map<String, MethodPair> methodMap, final String column,
            Class<? extends ItemAble> clazz) {

        String keyInCache = clazz.getName() + "-" + column;
        String keyColumn = atomicComputeKeyColumnCache.preGetAlreadyDoneResult(keyInCache);
        if (keyColumn != null) {
            return keyColumn;
        }
        keyColumn = atomicComputeKeyColumnCache.getComputeResult(keyInCache, new Callable<Object>() {
            @Override
            public String call() {
                for (String col : methodMap.keySet()) {
                    if (col.equalsIgnoreCase(column)) {
                        return col;
                    }
                }
                return "";
            }
        });
        return keyColumn;
    }

    /**
     * 生成 MethodMapper
     * 
     * @param clazz
     * @return Map<String, MethodPair>
     * @since 2015-7-28 by wangchongjie
     */
    @SuppressWarnings("unchecked")
    public static Map<String, MethodPair> generateMethodMapper(final Class<?> clazz) {
        if (methodMapperCache.containsKey(clazz)) {
            return (Map<String, MethodPair>) methodMapperCache.get(clazz).getValue();
        } else {
            GenerateMethodMapperFutureTask task = new GenerateMethodMapperFutureTask(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return generate(clazz);
                }
            });
            GenerateMethodMapperFutureTask old = methodMapperCache.putIfAbsent(clazz, task);
            if (old == null) {
                task.run();
            } else {
                task = old;
            }
            return (Map<String, MethodPair>) task.getValue();
        }
    }

    /**
     * 生成MethodPair
     * 
     * @param entityClass
     * @return Map<String, MethodPair> 
     * @since 2015-7-28 by wangchongjie
     */
    private static Map<String, MethodPair> generate(Class<?> entityClass) {

        // 获取该实体类的解析结果
        Set<Field> fields = ClassUtils.getAllFiled(entityClass);
        Set<Method> methods = ClassUtils.getAllMethod(entityClass);
        Map<String, Method> gsetterMap = ClassUtils.filter2Map(methods);

        Map<String, MethodPair> methodMap = new HashMap<String, MethodPair>();

        // 循环处理所有字段，过滤出该类加载为对象时需要调用的setter方法map
        for (Field f : fields) {
            // 静态字段则自动pass
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            // 不做关联加载的工作
            Class<?> fType = f.getType();
            if (fType.getName().startsWith("com.baidu.") || Collection.class.isAssignableFrom(fType) || fType.isArray()) {
                continue;
            }
            // 字段名字
            String name = f.getName();
            String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 其他字段获取field，getter，setter
            String setter = "set" + upperName;
            Method set = gsetterMap.get(setter);

            String getter = "get" + upperName;
            Method get = gsetterMap.get(getter);
            if (get == null) {
                get = gsetterMap.get("is" + upperName);
            }
            if (get == null || set == null) {
                continue;
            }
            // 获取字段的注解，如果没有，则从getter或者setter上获取注解
            OlapColumn column = OlapUtils.getColumnAnnotation(f, set, get);

            // 如果数据库映射字段不为空，则按照映射关系设置字段
            if (column == null) {
                methodMap.put(name, new MethodPair(set, get));
            } else if (column.value().equals(OlapConstants.COLUMN.IGNORE)) {
            } else if (StringUtils.isEmpty(column.alias())) {
                methodMap.put(column.value(), new MethodPair(set, get));
            } else {
                methodMap.put(column.alias(), new MethodPair(set, get));
            }
        }
        return methodMap;
    }

    public static Method getAfterAssembleMethod(Class<? extends ItemAble> entityClass) {

        for (Method method : entityClass.getMethods()) {
            if (method.getName().equalsIgnoreCase("afterAssemble")) {
                return method;
            }
        }
        assert false;
        return null; // 不会执行到此处
    }

    /**
     * 获取某个字段的annotation，从继承链最下面获取
     * 
     * @param f
     * @param set
     * @param get
     * @return
     */
    public static OlapColumn getColumnAnnotation(Field f, Method set, Method get) {

        // 三个地方都有可能出现column
        OlapColumn column = f.getAnnotation(OlapColumn.class);
        OlapColumn gColumn = get.getAnnotation(OlapColumn.class);
        OlapColumn sColumn = set.getAnnotation(OlapColumn.class);

        // 预先获取出get与set所在的类
        Class<?> sClass = set.getDeclaringClass();
        Class<?> gClass = get.getDeclaringClass();

        // 如果get上定义了annotation，且get定义的地方是子类
        if (gColumn != null && !gClass.isAssignableFrom(sClass)) {
            return gColumn;
        }

        // 如果是set上定义的annotation，且set方法不在父类中定义
        if (sColumn != null && !sClass.isAssignableFrom(gClass)) {
            return sColumn;
        }

        return column;
    }

    private static ConcurrentMap<Class<? extends ItemAble>, FetchAnnotationColumnFutureTask> olapAnnotationColumnCache =
            new ConcurrentHashMap<Class<? extends ItemAble>, FetchAnnotationColumnFutureTask>();

    /**
     * 获取olap表merge的key字段
     * 
     * @param clazz
     * @return 2014-8-26 下午1:33:56 created by wangchongjie
     */
    public static List<String> getOlapMergeKeyColumns(final Class<? extends ItemAble> clazz) {
        return getAnnotationColumns(clazz, OlapMergeKey.class);
    }

    /**
     * 获取olap表merge的value字段
     * 
     * @param clazz
     * @return 2014-11-18 下午5:47:20 created by wangchongjie
     */
    public static List<String> getOlapMergeValColumns(final Class<? extends ItemAble> clazz) {
        List<String> keyList = getAnnotationColumns(clazz, OlapMergeKey.class);
        List<String> allList = getAnnotationColumns(clazz, OlapColumn.class);
        if (CollectionUtils.isEmpty(allList)) {
            return null;
        }
        List<String> valList = new ArrayList<String>(allList);
        valList.removeAll(keyList);
        return valList;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getAnnotationColumns(final Class<? extends ItemAble> clazz,
            Class<? extends Annotation> annotType) {
        if (olapAnnotationColumnCache.containsKey(clazz)) {
            Map<Class<? extends Annotation>, List<String>> columnsMap =
                    (Map<Class<? extends Annotation>, List<String>>) olapAnnotationColumnCache.get(clazz).getValue();
            return columnsMap.get(annotType);
        } else {
            FetchAnnotationColumnFutureTask task = new FetchAnnotationColumnFutureTask(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return getAnnotationColumnsInternal(clazz);
                }
            });
            FetchAnnotationColumnFutureTask old = olapAnnotationColumnCache.putIfAbsent(clazz, task);
            if (old == null) {
                task.run();
            } else {
                task = old;
            }
            Map<Class<? extends Annotation>, List<String>> columnsMap =
                    (Map<Class<? extends Annotation>, List<String>>) task.getValue();
            return columnsMap.get(annotType);
        }
    }

    /**
     * 获取注解列的任务
     * 
     * @author wangchongjie
     * @fileName OlapUtils.java
     * @dateTime 2015-7-28 下午6:00:07
     */
    private static class FetchAnnotationColumnFutureTask extends FutureTask<Object> {
        public FetchAnnotationColumnFutureTask(Callable<Object> callable) {
            super(callable);
        }

        public Object getValue() {
            try {
                return get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取注解列
     * 
     * @param entityClass
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private static Map<Class<? extends Annotation>, List<String>> getAnnotationColumnsInternal(
            Class<? extends ItemAble> entityClass) {

        // 获取该实体类的解析结果
        Set<Field> fields = ClassUtils.getAllFiled(entityClass);
        Map<Class<? extends Annotation>, List<String>> columnsMap =
                new HashMap<Class<? extends Annotation>, List<String>>();

        // 循环处理所有字段，过滤出该类加载为对象时需要调用的setter方法map
        for (Field f : fields) {
            // 静态字段则自动pass
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            Annotation[] annots = f.getAnnotations();
            // 无注解则pass
            if (ArrayUtils.isArrayEmpty(annots)) {
                continue;
            }
            // 非olap列则pass
            OlapColumn olapColumn = f.getAnnotation(OlapColumn.class);
            String columnName = f.getName();
            if (null != olapColumn) {
                columnName = olapColumn.value();
            }
            for (Annotation annot : annots) {
                List<String> columns = columnsMap.get(annot.annotationType());
                if (columns == null) {
                    columns = new ArrayList<String>();
                    columnsMap.put(annot.annotationType(), columns);
                }
                columns.add(columnName);
            }
        }
        return columnsMap;
    }

    private static AtomicComputeCache<String, Map<String, String>> atomicComputeAlaisColsCache =
            new AtomicComputeCache<String, Map<String, String>>();

    /**
     * 获取反向别名字典
     * 
     * @param clazz
     * @return 别名alais->原始名称 2014-12-22 下午6:12:30 created by wangchongjie
     */
    public static Map<String, String> getReverseAlaisColumnDict(final Class<? extends ItemAble> clazz) {
        String keyInCache = clazz.getName();
        Map<String, String> alaisCol = atomicComputeAlaisColsCache.preGetAlreadyDoneResult(keyInCache);
        if (alaisCol != null) {
            return alaisCol;
        }
        alaisCol = atomicComputeAlaisColsCache.getComputeResult(keyInCache, new Callable<Object>() {
            @Override
            public Map<String, String> call() {
                return getAlaisColsInternal(clazz);
            }
        });
        return alaisCol;
    }

    /**
     * 填充别名字典
     * 
     * @param aliasCol
     * @param clazz
     * @param aliasColDict
     * @param reverseAliasColDict
     * @since 2015-7-28 by wangchongjie
     */
    public static void fillAliasColumnDict(String[] aliasCol, Class<? extends ItemAble> clazz,
            Map<String, String> aliasColDict, Map<String, String> reverseAliasColDict) {
        if (ArrayUtils.isArrayEmpty(aliasCol) || (aliasColDict == null && reverseAliasColDict == null)) {
            return;
        }
        Map<String, String> reverseAlaisColMap = OlapUtils.getReverseAlaisColumnDict(clazz);
        String oriColName;
        for (String alias : aliasCol) {
            oriColName = reverseAlaisColMap.get(alias);
            if (!StringUtils.isEmpty(oriColName)) {
                if (aliasColDict != null) {
                    aliasColDict.put(oriColName, alias);
                }
                if (reverseAliasColDict != null) {
                    reverseAliasColDict.put(alias, oriColName);
                }
            }
        }
    }

    private static Map<String, String> getAlaisColsInternal(Class<? extends ItemAble> entityClass) {

        // 获取该实体类的解析结果
        Set<Field> fields = ClassUtils.getAllFiled(entityClass);
        Map<String, String> alaisColMap = new HashMap<String, String>();

        // 循环处理所有字段，过滤出该类加载为对象时需要调用的setter方法map
        for (Field f : fields) {
            // 静态字段则自动pass
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            Annotation[] annots = f.getAnnotations();
            // 无注解则pass
            if (ArrayUtils.isArrayEmpty(annots)) {
                continue;
            }
            // 非olap列则pass
            OlapColumn olapColumn = f.getAnnotation(OlapColumn.class);
            if (null == olapColumn || StringUtils.isEmpty(olapColumn.alias())) {
                continue;
            }
            alaisColMap.put(olapColumn.alias(), olapColumn.value());
        }
        return alaisColMap;
    }

    /**
     * 数值范围过滤
     * 
     * @param filters
     * @param columnName
     * @param minId
     * @param maxId
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends Number> void makeNumberRangeFilter(List<String> filters, String columnName, T minId,
            T maxId) {
        if (minId != null && maxId != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(columnName);
            sb.append(" between ");
            sb.append(minId);
            sb.append(" and ");
            sb.append(maxId);
            filters.add(sb.toString());
        }
    }

    /**
     * 数值范围过滤
     * 
     * @param filters
     * @param columnName
     * @param id
     * @param op
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends Number> void makeNumberRangeFilter(List<String> filters, String columnName, T id,
            CompareType op) {
        if (id != null && op != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(columnName);
            sb.append(op.val());
            sb.append(id);
            filters.add(sb.toString());
        }
    }

    /**
     * 数值过滤
     * 
     * @param filters
     * @param columnName
     * @param id
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends Number> void makeNumberFilter(List<String> filters, String columnName, T id) {
        if (id != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(columnName);
            sb.append('=');
            sb.append(id);
            filters.add(sb.toString());
        }
    }

    /**
     * 数值过滤
     * 
     * @param filters
     * @param columnName
     * @param ids
     * @since 2015-7-28 by wangchongjie
     */
    public static void makeNumberFilter(List<String> filters, String columnName, Collection<? extends Number> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            StringBuffer sb = new StringBuffer();
            sb.append(columnName);
            if (ids.size() == 1) {
                sb.append('=');
                sb.append(ids.iterator().next());
                filters.add(sb.toString());
            } else {
                sb.append(" in (");
                sb.append(concatNumber(ids));
                sb.append(')');
                filters.add(sb.toString());
            }
        }
    }

    /**
     * 数值等值过滤
     * @param filters
     * @param columnName
     * @param id
     * @since 2015-7-28 by wangchongjie
     */
    public static void makeNumberEqualFilter(List<String> filters, String columnName, Number id) {
        makeNumberEqualFilter(filters, columnName, id, true);
    }

    /**
     * 数值不等值过滤
     * 
     * @param filters
     * @param columnName
     * @param id
     * @since 2015-7-28 by wangchongjie
     */
    public static void makeNumberNotEqualFilter(List<String> filters, String columnName, Number id) {
        makeNumberEqualFilter(filters, columnName, id, false);
    }

    /**
     * 数据等值过滤
     * 
     * @param filters
     * @param columnName
     * @param id
     * @param isEqual
     * @since 2015-7-28 by wangchongjie
     */
    private static void makeNumberEqualFilter(List<String> filters, String columnName, Number id, boolean isEqual) {
        if (id != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(columnName);
            if (isEqual) {
                sb.append('=');
                sb.append(id);
                filters.add(sb.toString());
            } else {
                sb.append("!=");
                sb.append(id);
                filters.add(sb.toString());
            }
        }
    }

    /**
     * 数值拼接成逗号分隔的字符串
     * 
     * @param keys
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static String concatNumber(Collection<? extends Number> keys) {
        StringBuffer res = new StringBuffer();
        for (Number key : keys) {
            res.append(key);
            res.append(',');
        }
        res.deleteCharAt(res.length() - 1);

        return res.toString();
    }
}
