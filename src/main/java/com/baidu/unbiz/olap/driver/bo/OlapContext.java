package com.baidu.unbiz.olap.driver.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.utils.OlapUtils;
import com.baidu.unbiz.olap.utils.StringUtils;

/**
 * Olap请求上下文环境
 * 
 * @author wangchongjie
 * @fileName OlapContext.java
 * @dateTime 2014-10-8 下午2:31:31
 */
public class OlapContext {

    /**
     * SQL占位符
     */
    public final AtomicInteger placeholderNumber = new AtomicInteger(0);

    /**
     * olap中以value做过滤条件
     */
    public final List<String> filterValueConditions = new ArrayList<String>();

    /**
     * 格式为:olap原始列名->olap列别名
     */
    private Map<String, String> aliasColDict;

    /**
     * 格式为:别名->olap原始列名
     */
    private Map<String, String> reverseAliasColDict;

    /**
     * 获取列别名
     * 
     * @return 列别名
     * @since 2015-7-28 by wangchongjie
     */
    public Set<String> getAliasCol() {
        return reverseAliasColDict.keySet();
    }

    /**
     * 获取原始列别名
     * 
     * @return 原始列别名
     * @since 2015-7-28 by wangchongjie
     */
    public Set<String> getOriAliasCol() {
        return aliasColDict.keySet();
    }

    /**
     * 构造方法
     * 
     * @param request
     */
    public OlapContext(OlapRequest<? extends ItemAble> request) {
        this.init(request);
    }

    /**
     *  构造方法
     */
    public OlapContext() {
    }

    /**
     * 上下文初始化
     * 
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    public void init(OlapRequest<? extends ItemAble> request) {
        this.processAliasColumnDict(request);
    }

    /**
     * 处理别名字典
     * 
     * @param request
     * @since 2015-7-28 by wangchongjie
     */
    private <T extends ItemAble> void processAliasColumnDict(OlapRequest<T> request) {

        String[] aliasCol = request.getAliasCols();
        Map<String, String> realAliasDict = new HashMap<String, String>();
        Map<String, String> reverseAliasDict = new HashMap<String, String>();
        OlapUtils.fillAliasColumnDict(aliasCol, request.getItemClazz(), realAliasDict, reverseAliasDict);

        this.aliasColDict = realAliasDict;
        this.reverseAliasColDict = reverseAliasDict;
    }

    /**
     * 不存在别名列
     * 
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    public boolean isAliasColsEmpty() {
        return this.aliasColDict.isEmpty() || this.reverseAliasColDict.isEmpty();
    }

    /**
     * 有则换为别名
     * 
     * @param columnName
     * @return 别名
     * @since 2015-7-28 by wangchongjie
     */
    public String alias(String columnName) {
        String alais = this.aliasColDict.get(columnName);
        if (StringUtils.isEmpty(alais)) {
            return columnName;
        }
        return alais;
    }

    /**
     * 有则换为原始列名
     * 
     * @param alias
     * @return 原始列名
     * @since 2015-7-28 by wangchongjie
     */
    public String deAlias(String alias) {
        String columnName = this.reverseAliasColDict.get(alias);
        if (StringUtils.isEmpty(columnName)) {
            return alias;
        }
        return columnName;
    }
}
