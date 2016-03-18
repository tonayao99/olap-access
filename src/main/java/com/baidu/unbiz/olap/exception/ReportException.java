package com.baidu.unbiz.olap.exception;

public class ReportException extends RuntimeException {

    private static final long serialVersionUID = 1935872558909331529L;

    public ReportException(String msg) {
        super(msg);
    }

    public ReportException(Throwable cause) {
        super(cause);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
