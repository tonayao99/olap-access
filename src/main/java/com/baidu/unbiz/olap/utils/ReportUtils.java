package com.baidu.unbiz.olap.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import com.baidu.unbiz.olap.codegen.CodeGenerator;
import com.baidu.unbiz.olap.codegen.OlapJoinerSupport;
import com.baidu.unbiz.olap.common.MethodPair;
import com.baidu.unbiz.olap.constant.SortOrder;
import com.baidu.unbiz.olap.log.AopLogFactory;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.service.ItemCompactHandler;
import com.baidu.unbiz.olap.service.ItemMergeHandler;

public class ReportUtils {

    protected static final Logger LOG = AopLogFactory.getLogger(ReportUtils.class);

    /**
     * 供service层merge业务字段使用 将字典信息填充至item list中
     * 
     * @param itemList
     * @param dictList
     * @param handler 2014-8-26 下午3:51:27 created by wangchongjie
     */
    public static <T, N> void fillItemFieldByDict(List<T> itemList, List<N> dictList, ItemMergeHandler<T, N> handler) {
        if (CollectionUtils.isEmpty(itemList) || CollectionUtils.isEmpty(dictList)) {
            return;
        }
        ReportUtils.fillItemFieldByDict(itemList, dictList, handler, null, null);
    }

    /**
     * 供service层merge业务字段使用 将字典信息填充至item list中
     * 
     * @param itemList
     * @param dictList
     * @param handler 2014-8-26 下午3:51:27 created by wangchongjie
     */
    public static <T, N, E> void fillItemFieldByDict(List<T> itemList, List<N> dictList1,
            ItemMergeHandler<? super T, N> handler1, List<E> dictList2, ItemMergeHandler<? super T, E> handler2) {
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }
        ReportUtils.fillItemFieldByDict(itemList, dictList1, handler1, dictList2, handler2, null, null);
    }

    /**
     * 供service层merge业务字段使用 将字典信息填充至item list中
     * 
     * @param itemList
     * @param dictList
     * @param handler 2014-8-26 下午3:51:27 created by wangchongjie
     */
    public static <T, N, E, F> void fillItemFieldByDict(List<T> itemList, List<N> dictList1,
            ItemMergeHandler<? super T, N> handler1, List<E> dictList2, ItemMergeHandler<? super T, E> handler2,
            List<F> dictList3, ItemMergeHandler<? super T, F> handler3) {
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }
        ReportUtils.fillItemFieldByDict(itemList, dictList1, handler1, dictList2, handler2, dictList3, handler3, null,
                null);
    }

    /**
     * 供service层merge业务字段使用 将字典信息填充至item list中
     * 
     * @param itemList
     * @param dictList
     * @param handler 2014-8-26 下午3:51:27 created by wangchongjie
     */
    public static <T, N, E, F, U> void fillItemFieldByDict(List<T> itemList, List<N> dictList1,
            ItemMergeHandler<? super T, N> handler1, List<E> dictList2, ItemMergeHandler<? super T, E> handler2,
            List<F> dictList3, ItemMergeHandler<? super T, F> handler3, List<U> dictList4,
            ItemMergeHandler<? super T, U> handler4) {
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }
        ReportUtils.fillItemFieldByDict(itemList, dictList1, handler1, dictList2, handler2, dictList3, handler3,
                dictList4, handler4, null, null);
    }

    /**
     * 供service层merge业务字段使用 将字典信息填充至item list中
     * 
     * @param itemList
     * @param dictList
     * @param handler 2014-8-26 下午3:51:27 created by wangchongjie
     */
    public static <T, N, E, F, U, K> void fillItemFieldByDict(List<T> itemList, List<N> dictList1,
            ItemMergeHandler<? super T, N> handler1, List<E> dictList2, ItemMergeHandler<? super T, E> handler2,
            List<F> dictList3, ItemMergeHandler<? super T, F> handler3, List<U> dictList4,
            ItemMergeHandler<? super T, U> handler4, List<K> dictList5, ItemMergeHandler<? super T, K> handler5) {

        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }

        Map<String, N> dictMap1 = getDictMap(dictList1, handler1);
        Map<String, E> dictMap2 = getDictMap(dictList2, handler2);
        Map<String, F> dictMap3 = getDictMap(dictList3, handler3);
        Map<String, U> dictMap4 = getDictMap(dictList4, handler4);
        Map<String, K> dictMap5 = getDictMap(dictList5, handler5);

        for (T item : itemList) {
            doMerge(handler1, dictMap1, item);
            doMerge(handler2, dictMap2, item);
            doMerge(handler3, dictMap3, item);
            doMerge(handler4, dictMap4, item);
            doMerge(handler5, dictMap5, item);
        }
    }

    /**
     * 获取字典Map
     * 
     * @param dictList
     * @param handler
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    private static <T, N> Map<String, N> getDictMap(List<N> dictList, ItemMergeHandler<T, N> handler) {
        Map<String, N> dictMap = null;
        if (handler != null && CollectionUtils.isNotEmpty(dictList)) {
            dictMap = new HashMap<String, N>(dictList.size());
            for (N dict : dictList) {
                dictMap.put(handler.getDictKey(dict), dict);
            }
        }
        return dictMap;
    }

    /**
     * 执行merge处理
     * 
     * @param handler
     * @param dictMap
     * @param item
     * @since 2015-7-28 by wangchongjie
     */
    private static <T, N> void doMerge(ItemMergeHandler<T, N> handler, Map<String, N> dictMap, T item) {
        if (handler != null && dictMap != null) {
            String mergeKey = handler.getItemKey(item);
            N dict = dictMap.get(mergeKey);
            handler.mergeDictInfoToItem(item, dict);
        }
    }

    /**
     * 供service层list行转列使用 将list的稀疏的多行压缩为完整的一行
     * 
     * @param itemList
     * @param handler
     * @return 2014-8-29 下午3:48:45 created by wangchongjie
     */
    public static <T> List<T> compactItemList(List<T> itemList, ItemCompactHandler<T> handler) {
        if (CollectionUtils.isEmpty(itemList) || handler == null) {
            return itemList;
        }
        Map<String, T> resultMap = new HashMap<String, T>(itemList.size());
        for (T item : itemList) {
            String mergeKey = handler.getItemKey(item);
            T old = resultMap.get(mergeKey);
            if (old == null) {
                old = item;
                resultMap.put(mergeKey, old);
            } else {
                handler.valueCompact(old, item);
            }
        }
        for (T item : resultMap.values()) {
            handler.afterCompact(item);
        }
        itemList.clear();
        itemList.addAll(resultMap.values());
        return itemList;
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVal
     * @param clazz
     * @param appendDisMatchItem
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> List<T> joinItemList(List<T> mainList, List<T> subList, String mergeKey,
            String mergeVal, Class<?> clazz, boolean appendDisMatchItem) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(mergeVal);
        return joinItemList(mainList, subList, mergeKeys, mergeVals, clazz, SortOrder.DESC, appendDisMatchItem);
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVals
     * @param clazz
     * @param appendDisMatchItem
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> List<T> joinItemList(List<T> mainList, List<T> subList, String mergeKey,
            Set<String> mergeVals, Class<?> clazz, boolean appendDisMatchItem) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        return joinItemList(mainList, subList, mergeKeys, mergeVals, clazz, SortOrder.DESC, appendDisMatchItem);
    }
    
    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @param appendDisMatchItem
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> List<T> joinItemList(List<T> mainList, List<T> subList, Set<String> mergeKeys,
            Set<String> mergeVals, Class<?> clazz, boolean appendDisMatchItem) {

        return joinItemList(mainList, subList, mergeKeys, mergeVals, clazz, SortOrder.DESC, appendDisMatchItem);
    }

    /**
     * Code generator方式实现，效率比 mergeItemList的反射实现方式高 将两个list按指定keys字段merger成一个list;
     * 按mergeKeys为keys，将subList的mergeVals，merge到mainList中; 若appendDisMatchItem为true，则将不匹配的记录也添加到结果集中;
     * order为倒叙则不匹配的结果集将追加到result的末尾，正顺则放开头;
     * 
     * @param mainList
     * @param subList
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @return
     * @throws Exception 2014-12-17 下午5:17:34 created by wangchongjie
     */
    public static <T> List<T> joinItemList(List<T> mainList, List<T> subList, Set<String> mergeKeys,
            Set<String> mergeVals, Class<?> clazz, SortOrder order, boolean appendDisMatchItem) {
        if (CollectionUtils.isEmpty(mergeKeys) || CollectionUtils.isEmpty(mergeVals) || clazz == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(mainList) && CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>();
        }
        if (CollectionUtils.isEmpty(mainList)) {
            if (appendDisMatchItem) {
                return new ArrayList<T>(subList);
            } else {
                return new ArrayList<T>();
            }
        } else if (CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>(mainList);
        }
        List<T> finalList = new ArrayList<T>(mainList);
        Map<String, T> itemMap = new HashMap<String, T>();

        OlapJoinerSupport joiner = CodeGenerator.getOlapJoinerSupportClass(mergeKeys, mergeVals, clazz);

        try {
            for (T item : subList) {
                String multiKey = joiner.getKeys(item);
                itemMap.put(multiKey, item);
            }
            for (T item : finalList) {
                String multiKey = joiner.getKeys(item);
                T subItem = itemMap.get(multiKey);
                if (subItem != null) {
                    joiner.setValues(subItem, item);
                    itemMap.remove(multiKey);
                }
            }
            if (appendDisMatchItem) {
                if (order == SortOrder.DESC) {
                    finalList.addAll(itemMap.values());
                }
                if (order == SortOrder.ASC) {
                    List<T> tmpList = new ArrayList<T>(itemMap.values());
                    tmpList.addAll(finalList);
                    finalList = tmpList;
                }
            }
        } catch (IllegalArgumentException e) {
            LOG.error("join fail: ", e);
        }

        return finalList;
    }
    
    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVal
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @param keepMainListOrder
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> List<T> joinItemListKeepOrder(List<T> mainList, List<T> subList,
            String mergeKey, String mergeVal, Class<?> clazz, SortOrder order,
            boolean appendDisMatchItem, boolean keepMainListOrder) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(mergeVal);
        return joinItemListKeepOrder(mainList, subList, mergeKeys, mergeVals, clazz, order, appendDisMatchItem,
                keepMainListOrder);
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVals
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @param keepMainListOrder
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T> List<T> joinItemListKeepOrder(List<T> mainList, List<T> subList,
            String mergeKey, Set<String> mergeVals, Class<?> clazz, SortOrder order,
            boolean appendDisMatchItem, boolean keepMainListOrder) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        return joinItemListKeepOrder(mainList, subList, mergeKeys, mergeVals, clazz, order, appendDisMatchItem,
                keepMainListOrder);
    }

    /**
     * Code generator方式实现，效率比 mergeItemListKeepOrder的反射实现方式高 保序merge，
     * keepMainListOrder可控制以哪个list的顺序为准
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVals
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @param keepMainListOrder
     * @return 2014-12-17 下午5:34:02 created by wangchongjie
     */
    public static <T> List<T> joinItemListKeepOrder(List<T> mainList, List<T> subList,
            Set<String> mergeKey, Set<String> mergeVals, Class<?> clazz, SortOrder order,
            boolean appendDisMatchItem, boolean keepMainListOrder) {

        if (keepMainListOrder) {
            return joinItemList(mainList, subList, mergeKey, mergeVals, clazz, order, appendDisMatchItem);
        } else {
            return joinItemListKeepSubListOrder(mainList, subList, mergeKey, mergeVals, clazz, order,
                    appendDisMatchItem);
        }
    }

   
    /**
     * Code generator方式实现，效率比 mergeItemList的反射实现方式高 保持sublist的顺序的方式merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @return 2014-12-17 下午5:26:07 created by wangchongjie
     */
    public static <T> List<T> joinItemListKeepSubListOrder(List<T> mainList, List<T> subList,
            Set<String> mergeKeys, Set<String> mergeVals, Class<?> clazz, SortOrder order,
            boolean appendDisMatchItem) {
        if (CollectionUtils.isEmpty(mergeKeys) || CollectionUtils.isEmpty(mergeVals) || clazz == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(mainList) && CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>();
        }
        if (CollectionUtils.isEmpty(mainList)) {
            return new ArrayList<T>(subList);
        } else if (CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>(mainList);
        }

        List<T> finalList = new ArrayList<T>();
        Map<String, T> itemMap = new HashMap<String, T>();
        OlapJoinerSupport joiner = CodeGenerator.getOlapJoinerSupportClass(mergeKeys, mergeVals, clazz);

        try {
            // 将主表处理成key为联合key，value为次表item的map，供join使用
            for (T item : mainList) {
                String multiKey = joiner.getKeys(item);
                itemMap.put(multiKey, item);
            }

            // 向主表的item中填充此表的value字段
            for (T item : subList) {
                String multiKey = joiner.getKeys(item);
                T mainItem = itemMap.get(multiKey);
                if (mainItem != null) {
                    // 逐一字段merge
                    joiner.setValues(item, mainItem);
                    finalList.add(mainItem);
                    itemMap.remove(multiKey);
                } else {
                    finalList.add(item);
                }
            }
            if (appendDisMatchItem) {
                if (order == SortOrder.DESC) {
                    finalList.addAll(itemMap.values());
                }
                if (order == SortOrder.ASC) {
                    List<T> tmpList = new ArrayList<T>(itemMap.values());
                    tmpList.addAll(finalList);
                    finalList = tmpList;
                }
            }
        } catch (IllegalArgumentException e) {
            LOG.error("join fail: ", e);
        }
        return finalList;
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVal
     * @param clazz
     * @param appendDisMatchItem
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemList(List<T> mainList, List<T> subList, String mergeKey,
            String mergeVal, Class<? extends ItemAble> clazz, boolean appendDisMatchItem) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(mergeVal);
        return mergeItemList(mainList, subList, mergeKeys, mergeVals, clazz, SortOrder.DESC, appendDisMatchItem);
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVals
     * @param clazz
     * @param appendDisMatchItem
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemList(List<T> mainList, List<T> subList, String mergeKey,
            Set<String> mergeVals, Class<? extends ItemAble> clazz, boolean appendDisMatchItem) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        return mergeItemList(mainList, subList, mergeKeys, mergeVals, clazz, SortOrder.DESC, appendDisMatchItem);
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @param appendDisMatchItem
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemList(List<T> mainList, List<T> subList, Set<String> mergeKeys,
            Set<String> mergeVals, Class<? extends ItemAble> clazz, boolean appendDisMatchItem) {

        return mergeItemList(mainList, subList, mergeKeys, mergeVals, clazz, SortOrder.DESC, appendDisMatchItem);
    }

    /**
     * 将两个list按指定keys字段merger成一个list; 按mergeKeys为keys，将subList的mergeVals，merge到mainList中;
     * 若appendDisMatchItem为true，则将不匹配的记录也添加到结果集中; order为倒叙则不匹配的结果集将追加到result的末尾，正顺则放开头;
     * 
     * @param mainList
     * @param subList
     * @param mergeKeys
     * @param mergeVals
     * @param clazz List范型的类
     * @param order
     * @param appendDisMatchItem
     * @return 2014-7-31 下午1:45:44 created by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemList(List<T> mainList, List<T> subList, Set<String> mergeKeys,
            Set<String> mergeVals, Class<? extends ItemAble> clazz, SortOrder order, boolean appendDisMatchItem) {
        if (CollectionUtils.isEmpty(mergeKeys) || CollectionUtils.isEmpty(mergeVals) || clazz == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(mainList) && CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>();
        }
        if (CollectionUtils.isEmpty(mainList)) {
            if (appendDisMatchItem) {
                return new ArrayList<T>(subList);
            } else {
                return new ArrayList<T>();
            }
        } else if (CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>(mainList);
        }

        List<T> finalList = new ArrayList<T>(mainList);
        Map<String, MethodPair> methodMap = OlapUtils.generateMethodMapper(clazz);
        Map<String, T> itemMap = new HashMap<String, T>();

        try {
            // 将次表处理成key为联合key，value为次表item的map，供join使用
            for (T item : subList) {
                String multiKey = "";
                for (String key : mergeKeys) {
                    MethodPair methods = methodMap.get(key);
                    Method getter = methods.getter;
                    Object res = getter.invoke(item);
                    multiKey = multiKey + "-" + (res == null ? "" : res.toString());
                }
                itemMap.put(multiKey, item);
            }

            // 向主表的item中填充此表的value字段
            for (T item : finalList) {
                String multiKey = "";
                for (String key : mergeKeys) {
                    MethodPair methods = methodMap.get(key);
                    methods = (methods == null) ? methodMap.get((key = key.toLowerCase())) : methods;
                    if (methods == null) {
                        LOG.error("Error in " + key + " , no getter found in class: " + clazz.getName());
                        continue;
                    }
                    Method getter = methods.getter;
                    Object res = getter.invoke(item);
                    multiKey = multiKey + "-" + (res == null ? "" : res.toString());
                }
                T subItem = itemMap.get(multiKey);
                if (subItem != null) {
                    // 逐一字段merge
                    for (String val : mergeVals) {
                        MethodPair methods = methodMap.get(val);
                        methods = (methods == null) ? methodMap.get((val = val.toLowerCase())) : methods;

                        Method getter = methods.getter;
                        Object res = getter.invoke(subItem);

                        Method setter = methods.setter;
                        setter.invoke(item, res);
                    }
                    itemMap.remove(multiKey);
                }
            }
            if (appendDisMatchItem) {
                if (order == SortOrder.DESC) {
                    finalList.addAll(itemMap.values());
                }
                if (order == SortOrder.ASC) {
                    List<T> tmpList = new ArrayList<T>(itemMap.values());
                    tmpList.addAll(finalList);
                    finalList = tmpList;
                }
            }

        } catch (IllegalArgumentException e) {
            LOG.error("merge fail: ", e);
        } catch (IllegalAccessException e) {
            LOG.error("merge fail: ", e);
        } catch (InvocationTargetException e) {
            LOG.error("merge fail: ", e);
        }
        return finalList;
    }
    
    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVal
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @param keepMainListOrder
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemListKeepOrder(List<T> mainList, List<T> subList,
            String mergeKey, String mergeVal, Class<? extends ItemAble> clazz, SortOrder order,
            boolean appendDisMatchItem, boolean keepMainListOrder) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        Set<String> mergeVals = new HashSet<String>();
        mergeVals.add(mergeVal);
        return mergeItemListKeepOrder(mainList, subList, mergeKeys, mergeVals, clazz, order, appendDisMatchItem,
                keepMainListOrder);
    }

    /**
     * Item列表merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVals
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @param keepMainListOrder
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemListKeepOrder(List<T> mainList, List<T> subList,
            String mergeKey, Set<String> mergeVals, Class<? extends ItemAble> clazz, SortOrder order,
            boolean appendDisMatchItem, boolean keepMainListOrder) {

        Set<String> mergeKeys = new HashSet<String>();
        mergeKeys.add(mergeKey);
        return mergeItemListKeepOrder(mainList, subList, mergeKeys, mergeVals, clazz, order, appendDisMatchItem,
                keepMainListOrder);
    }

    /**
     * 保序merge，keepMainListOrder可控制以哪个list的顺序为准
     * 
     * @param mainList
     * @param subList
     * @param mergeKey
     * @param mergeVals
     * @param clazz
     * @param order
     * @param appendDisMatchItem
     * @param keepMainListOrder
     * @return 2014-7-31 下午1:51:16 created by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemListKeepOrder(List<T> mainList, List<T> subList,
            Set<String> mergeKey, Set<String> mergeVals, Class<? extends ItemAble> clazz, SortOrder order,
            boolean appendDisMatchItem, boolean keepMainListOrder) {

        if (keepMainListOrder) {
            return mergeItemList(mainList, subList, mergeKey, mergeVals, clazz, order, appendDisMatchItem);
        } else {
            return mergeItemListKeepSubListOrder(mainList, subList, mergeKey, mergeVals, clazz, order,
                    appendDisMatchItem);
        }
    }

    /**
     * 保持sublist的顺序的方式merge
     * 
     * @param mainList
     * @param subList
     * @param mergeKeys
     * @param mergeVals
     * @param clazz
     * @param appendDisMatchItem
     * @return 2014-7-21 下午4:18:21 created by wangchongjie
     */
    public static <T extends ItemAble> List<T> mergeItemListKeepSubListOrder(List<T> mainList, List<T> subList,
            Set<String> mergeKeys, Set<String> mergeVals, Class<? extends ItemAble> clazz, SortOrder order,
            boolean appendDisMatchItem) {
        if (CollectionUtils.isEmpty(mergeKeys) || CollectionUtils.isEmpty(mergeVals) || clazz == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(mainList) && CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>();
        }
        if (CollectionUtils.isEmpty(mainList)) {
            return new ArrayList<T>(subList);
        } else if (CollectionUtils.isEmpty(subList)) {
            return new ArrayList<T>(mainList);
        }

        List<T> finalList = new ArrayList<T>();
        Map<String, MethodPair> methodMap = OlapUtils.generateMethodMapper(clazz);
        Map<String, T> itemMap = new HashMap<String, T>();

        try {
            // 将主表处理成key为联合key，value为次表item的map，供join使用
            for (T item : mainList) {
                String multiKey = "";
                for (String key : mergeKeys) {
                    MethodPair methods = methodMap.get(key);
                    Method getter = methods.getter;
                    Object res = getter.invoke(item);
                    multiKey = multiKey + "-" + res.toString();
                }
                itemMap.put(multiKey, item);
            }

            // 向主表的item中填充此表的value字段
            for (T item : subList) {
                String multiKey = "";
                for (String key : mergeKeys) {
                    MethodPair methods = methodMap.get(key);
                    methods = (methods == null) ? methodMap.get((key = key.toLowerCase())) : methods;
                    if (methods == null) {
                        LOG.error("Error in " + key + " , no getter found in class: " + clazz.getName());
                        continue;
                    }
                    Method getter = methods.getter;
                    Object res = getter.invoke(item);
                    multiKey = multiKey + "-" + res.toString();
                }
                T mainItem = itemMap.get(multiKey);
                if (mainItem != null) {
                    // 逐一字段merge
                    for (String val : mergeVals) {
                        MethodPair methods = methodMap.get(val);
                        methods = (methods == null) ? methodMap.get((val = val.toLowerCase())) : methods;

                        Method getter = methods.getter;
                        Object res = getter.invoke(item);

                        Method setter = methods.setter;
                        setter.invoke(mainItem, res);
                    }
                    finalList.add(mainItem);
                    itemMap.remove(multiKey);
                } else {
                    finalList.add(item);
                }
            }
            if (appendDisMatchItem) {
                if (order == SortOrder.DESC) {
                    finalList.addAll(itemMap.values());
                }
                if (order == SortOrder.ASC) {
                    List<T> tmpList = new ArrayList<T>();
                    tmpList.addAll(itemMap.values());
                    tmpList.addAll(finalList);
                    finalList = tmpList;
                }
            }
        } catch (IllegalArgumentException e) {
            LOG.error("merge fail: ", e);
        } catch (IllegalAccessException e) {
            LOG.error("merge fail: ", e);
        } catch (InvocationTargetException e) {
            LOG.error("merge fail: ", e);
        }
        return finalList;
    }
}
