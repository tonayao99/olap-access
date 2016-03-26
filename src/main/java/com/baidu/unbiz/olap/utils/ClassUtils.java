package com.baidu.unbiz.olap.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.baidu.unbiz.olap.annotation.OlapColumn;

/**
 * Class相关工具类
 * 
 * @author wangchongjie
 * @fileName ClassUtils.java
 * @dateTime 2014-7-16 下午3:02:20
 */
public class ClassUtils {

    /**
     * 获取某个类锁指定的泛型参数数组
     * 
     * @param c
     * @return
     */
    public final static Type[] getGenericTypes(Class<?> c) {
        Type superClass = c.getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) superClass;
        return type.getActualTypeArguments();
    }

    /**
     * 获取一个类的所有字段
     * 
     * @param entityClass
     * @return
     */
    public static Set<Field> getAllFiled(Class<?> entityClass) {

        // 获取本类的所有字段
        Set<Field> fs = new HashSet<Field>();
        for (Field f : entityClass.getFields()) {
            fs.add(f);
        }
        for (Field f : entityClass.getDeclaredFields()) {
            fs.add(f);
        }

        // 递归获取父类的所有字段
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            Set<Field> superFileds = getAllFiled(superClass);
            fs.addAll(superFileds);
        }

        return fs;
    }

    /**
     * 获取一个类的所有方法
     * 
     * @param entityClass
     * @return
     */
    public static Set<Method> getAllMethod(Class<?> entityClass) {

        // 获取本类的所有的方法
        Set<Method> ms = new HashSet<Method>();
        for (Method m : entityClass.getMethods()) {
            ms.add(m);
        }
        for (Method m : entityClass.getDeclaredMethods()) {
            ms.add(m);
        }

        // 递归获取父类的所有方法
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            Set<Method> superFileds = getAllMethod(superClass);
            ms.addAll(superFileds);
        }

        return ms;
    }

    /**
     * 获取非static的public的getter、setter方法
     * 
     * @param ms
     * @return 2014-7-16 下午3:03:04 created by wangchongjie
     */
    public static Map<String, Method> filter2Map(Set<Method> ms) {

        Map<String, Method> map = new HashMap<String, Method>();
        Set<Method> sameNameSetters = new HashSet<Method>();
        for (Method m : ms) {
            boolean flag = Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers());
            if (flag) {

                String name = m.getName();
                if (name.startsWith("get") && m.getParameterTypes().length == 0) {
                } else if (name.startsWith("is") && m.getParameterTypes().length == 0) {
                } else if (name.startsWith("set") && m.getParameterTypes().length == 1) {
                } else if (name.equalsIgnoreCase("setUnixTime") // add by wangchongjie for OlapEngine
                        && m.getParameterTypes().length == 2) {
                } else {
                    continue;
                }

                // 获取同名的方法
                Method old = map.get(name);

                // 如果之前没有同名方法,则添加本方法
                if (old == null) {
                    map.put(name, m);

                    // 如果有同名方法，且本方法在子类中声明，且，父类本方法包含了annotation，则替换原来的方法
                } else if (old.getDeclaringClass().isAssignableFrom(m.getDeclaringClass())
                        && m.getAnnotation(OlapColumn.class) != null) {
                    map.put(name, m);
                    // 重名setter存储备用
                } else if (name.startsWith("set")) {
                    sameNameSetters.add(m);
                }
            }
        }
        // 修正getter和setter类型不匹配
        for (Method setter : sameNameSetters) {
            String name = setter.getName();
            Method oldSetter = map.get(name);

            Method getter = map.get(name.replaceFirst("set", "get"));
            if (getter == null) {
                getter = map.get(name.replaceFirst("set", "is"));
            }
            if (getter == null) {
                continue;
            }
            // 如果原get、set方法的参数不匹配，则替换原来的方法
            if (!oldSetter.getParameterTypes()[0].equals(getter.getGenericReturnType())
                    && setter.getParameterTypes()[0].equals(getter.getGenericReturnType())) {
                map.put(name, setter);
            } else if (oldSetter.getDeclaringClass().isAssignableFrom(setter.getDeclaringClass())
                    && setter.getAnnotation(OlapColumn.class) != null) {
                map.put(name, setter);
            }
        }
        return map;
    }

    /**
     * 将from的属性copy到to中
     * 
     * @param from
     * @param to
     * @since 2015-7-15 by wangchongjie
     */
    public final static void copyProperties(Object from, Object to) {

        Set<Field> fromSet = getAllFiled(from.getClass());
        Set<Field> toSet = getAllFiled(to.getClass());

        Map<String, Field> toMap = new HashMap<String, Field>();
        for (Field f : toSet) {
            toMap.put(f.getName(), f);
        }

        for (Field f : fromSet) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            String name = f.getName();
            Field toField = toMap.get(name);
            if (toField == null) {
                continue;
            }

            toField.setAccessible(true);
            f.setAccessible(true);
            try {
                toField.set(to, f.get(from));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取一个类的field
     * 
     * @param field
     * @param clazz
     * @return
     * @since 2015-7-15 by wangchongjie
     */
    public static Field getFieldFromClass(String field, Class<? extends Object> clazz) {
        try {
            return clazz.getDeclaredField(field);
        } catch (Exception e) {
            try {
                return clazz.getField(field);
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
     * key为clazz+fieldName,value为Field对象
     */
    private static Map<String, Field> fieldCache = new ConcurrentHashMap<String, Field>();

    /**
     * field有效并缓存
     * 
     * @param field
     * @param cacheKey
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private static boolean fieldIsEffectAndCache(Field field, String cacheKey) {
        if (field != null) {
            fieldCache.put(cacheKey, field);
            return true;
        }
        return false;
    }

    /**
     * 遍历所有field（父子类、public、protected、private）
     * 
     * @param clazz
     * @param field
     * @return 2014-11-21 下午7:37:52 created by wangchongjie
     */
    public static Field getCachedFieldFromClass(String field, Class<?> clazz) {
        String cacheKey = clazz + "|" + field;
        Field result = fieldCache.get(cacheKey);
        if (result != null) {
            return result;
        }
        try {
            result = clazz.getDeclaredField(field);
        } catch (Exception e) {
        }
        if (fieldIsEffectAndCache(result, cacheKey)) {
            return result;
        }
        try {
            result = clazz.getField(field);
        } catch (Exception ex) {
        }
        if (fieldIsEffectAndCache(result, cacheKey)) {
            return result;
        }
        // 递归获取父类的所有字段
        Class<?> superClass = clazz.getSuperclass();
        if (!superClass.equals(Object.class)) {
            result = getCachedFieldFromClass(field, superClass);
        } else {
            return null;
        }
        if (fieldIsEffectAndCache(result, cacheKey)) {
            return result;
        }
        return null;
    }

}
