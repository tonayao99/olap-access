package com.baidu.unbiz.olap.cache;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.baidu.unbiz.olap.log.AopLogFactory;

/**
 * 异步缓存任务工具类
 * 
 * @author wangchongjie
 * @fileName AsynTaskUtils.java
 * @dateTime 2014-6-3 上午11:04:09
 */
public class AsynTaskUtils {

    private static final Logger LOG = AopLogFactory.getLogger(AsynTaskUtils.class);

    private static final int MIN_TASK_NUM = Math.min(3, Runtime.getRuntime().availableProcessors());
    private static final int MAX_TASK_NUM = Math.min(16, Runtime.getRuntime().availableProcessors());
    private static final int MAX_CACHE_TASK_NUM = 1000;

    private static final BlockingQueue<Runnable> TASK_QUEUE = new ArrayBlockingQueue<Runnable>(MAX_CACHE_TASK_NUM);

    /**
     * 此处若缓存队列溢出，则直接抛弃
     */
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            MIN_TASK_NUM, MAX_TASK_NUM, 10, TimeUnit.SECONDS,
            TASK_QUEUE, new CustomizableThreadFactory("olap-cache-pool-"), 
            new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 执行Task
     * 
     * @param task
     * @since 2015-7-28 by wangchongjie
     */
    public static void runTask(Runnable task) {
        // 避免queue满后抛异常
        // while(taskQueue.size() >= MaxCacheTaskNum){
        // sleep(20);
        // LOG.info("Thread Pool Blocking Queue Is Full.");
        // }
        threadPool.execute(task);
    }
    
    /**
     * 休眠一段时间
     * 
     * @param miliSeconds
     * @since 2015-7-28 by wangchongjie
     */
    public static final void sleep(int miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            LOG.error("Thread Sleep Is Interrupted", e);
        }
    }
}
