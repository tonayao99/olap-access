package com.baidu.unbiz.olap.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.baidu.unbiz.olap.cache.AsynCacheTask;
import com.baidu.unbiz.olap.cache.AsynTaskUtils;
import com.baidu.unbiz.olap.cache.OlapCacheHandler;
import com.baidu.unbiz.olap.driver.bo.ReportRequest;
import com.baidu.unbiz.olap.obj.ItemAble;
import com.baidu.unbiz.olap.utils.DateUtils;
import com.baidu.unbiz.olap.utils.Fs64Utils;

@Component
public class OlapCacheWrapper {

    /**
     * 是否需要缓存
     * 
     * @param request
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    @SuppressWarnings("rawtypes")
    protected boolean shouldCache(ReportRequest request) {
        if (null == request) {
            return false;
        }
        Date to = request.getTo();
        if (DateUtils.getHourSpan(to, new Date()) < 16) {
            return false;
        }
        Date from = request.getFrom();
        if (DateUtils.getHourSpan(from, new Date()) < 16) {
            return false;
        }
        return true;
    }

    /**
     * 去除缓存无关字段
     * 
     * @param str
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    protected String mockUselessFieldForCount(String str) {
        return str.replaceAll("page=[^,]*,?", "").replaceAll("pageSize=[^,]*,?", "")
                .replaceAll("orderPairs=[^,]*,?", "");

    }

    /**
     * 从缓存中查询结果
     * 
     * @param rr
     * @param handler
     * @return List<T>
     * @since 2015-7-28 by wangchongjie
     */
    @SuppressWarnings("unchecked")
    protected <T extends ItemAble> List<T> queryFromCache(ReportRequest<T> rr, OlapCacheHandler handler) {
        if (handler != null) {
            @SuppressWarnings("static-access")
            String cacheKey = Fs64Utils.signFs64(handler.QUERY_PREFIX + rr.toString()).toString();
            return (List<T>) handler.queryFromCache(cacheKey);
        }
        return null;
    }

    /**
     * 将结果写入缓存
     * 
     * @param rr
     * @param result
     * @param handler
     * @since 2015-7-28 by wangchongjie
     */
    protected <T extends ItemAble> void putIntoCache(ReportRequest<T> rr, List<T> result, OlapCacheHandler handler) {
        if (handler != null && this.shouldCache(rr)) {
            AsynTaskUtils.runTask(new AsynCacheTask(rr.toString(), result, handler));
        }
    }

    /**
     * 从缓存中获取条数
     * 
     * @param rr
     * @param handler
     * @return
     * @since 2015-7-28 by wangchongjie
     */
    protected <T extends ItemAble> Integer countFromCache(ReportRequest<T> rr, OlapCacheHandler handler) {
        if (handler != null) {
            @SuppressWarnings("static-access")
            String cacheKey = Fs64Utils.signFs64(mockUselessFieldForCount(handler.COUNT_PREFIX + rr)).toString();
            return (Integer) handler.countFromCache(cacheKey);
        }
        return null;
    }

    /**
     * 将条数写入缓存
     * 
     * @param rr
     * @param count
     * @param handler
     * @since 2015-7-28 by wangchongjie
     */
    protected <T extends ItemAble> void putCountIntoCache(
            ReportRequest<T> rr, Integer count, OlapCacheHandler handler) {
        if (handler != null && this.shouldCache(rr)) {
            @SuppressWarnings("static-access")
            String cacheKey = Fs64Utils.signFs64(mockUselessFieldForCount(handler.COUNT_PREFIX + rr)).toString();
            handler.putCountIntoCache(cacheKey, count);
        }
    }

}
