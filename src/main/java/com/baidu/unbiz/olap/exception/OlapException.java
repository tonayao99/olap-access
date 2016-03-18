package com.baidu.unbiz.olap.exception;

public class OlapException extends RuntimeException {

    private static final long serialVersionUID = 99517992785875413L;

    public OlapException(String msg) {
        super(msg);
    }

    public OlapException(Throwable cause) {
        super(cause);
    }

    public OlapException(String message, Throwable cause) {
        super(message, cause);
    }
}
