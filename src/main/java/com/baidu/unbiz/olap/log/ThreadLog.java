package com.baidu.unbiz.olap.log;

import com.baidu.unbiz.olap.common.ThreadContext;

/**
 * 线程日志级别的控制
 * 
 * @author wangchongjie
 * @fileName ThreadLog.java
 * @dateTime 2015-7-15 下午3:17:06
 */
public class ThreadLog {
    public static final int LEVEL_TRACE = 10;
    public static final int LEVEL_DEBUG = 20;
    public static final int LEVEL_INFO = 30;
    public static final int LEVEL_WARN = 40;
    public static final int LEVEL_ERROR = 50;

    public static final boolean isTraceEnabled() {
        return ThreadContext.getThreadLog() != null && ThreadContext.getThreadLog() >= LEVEL_TRACE;
    }

    public static final boolean isDebugEnabled() {
        return ThreadContext.getThreadLog() != null && ThreadContext.getThreadLog() >= LEVEL_DEBUG;
    }

    public static final boolean isInfoEnabled() {
        return ThreadContext.getThreadLog() != null && ThreadContext.getThreadLog() >= LEVEL_INFO;
    }

    public static final boolean isWarnEnabled() {
        return ThreadContext.getThreadLog() != null && ThreadContext.getThreadLog() >= LEVEL_WARN;
    }

    public static final boolean isErrorEnabled() {
        return ThreadContext.getThreadLog() != null && ThreadContext.getThreadLog() >= LEVEL_ERROR;
    }
}
