package com.evencyan.exception;

import com.evencyan.controller.Code;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemException extends RuntimeException {

    private Code code;
    private Object data;
    private String msg;

    public SystemException(Code code, Object data, String msg) {
        super(msg);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public SystemException(Code code, Object data, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
