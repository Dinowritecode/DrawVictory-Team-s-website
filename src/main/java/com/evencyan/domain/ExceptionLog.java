package com.evencyan.domain;

import lombok.Data;

@Data
public class ExceptionLog {
    private Integer id,type;
    private String logInfo;

    public ExceptionLog(String logInfo, Integer type) {
        this.type = type;
        this.logInfo = logInfo;
    }
}
