package com.evencyan.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private int code;
    private Object data;
    private String msg;

    public BusinessException(int code, Object data, String msg) {
        super(msg);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BusinessException(int code, Object data, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
