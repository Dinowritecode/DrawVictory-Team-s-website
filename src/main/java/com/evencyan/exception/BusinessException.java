package com.evencyan.exception;

import com.evencyan.controller.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BusinessException extends RuntimeException {

    private Code code;
    private Object data;
    private String msg;

    public BusinessException(Code code, Object data, String msg) {
        super(msg);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BusinessException(Code code, Object data, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
