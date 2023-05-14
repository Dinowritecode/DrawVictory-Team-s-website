package com.evencyan.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result {
    int code;
    Object data;
    String msg;

    public Result(int code, Object data) {
        this.code = code;
        this.data = data;
    }

}
