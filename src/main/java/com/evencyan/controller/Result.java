package com.evencyan.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result {
    Code code;
    Object data;
    String msg;
    Boolean success;

    public static <T> Result success(Code code, T data, String msg) {
        return new Result(code, data, msg, true);
    }

    public static Result failure(Code code, Object data, String msg) {
        return new Result(code, data, msg, false);
    }
}
