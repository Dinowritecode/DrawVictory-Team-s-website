package com.evencyan.exception;

import lombok.Data;

@Data
public class SystemException extends RuntimeException {

    private int code;
    private Object data;
    private String msg;

    public SystemException(int code, Object data, String msg) {
        super(msg);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public SystemException(int code, Object data, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
