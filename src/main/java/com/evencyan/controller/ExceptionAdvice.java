package com.evencyan.controller;

import com.evencyan.exception.BusinessException;
import com.evencyan.thread.ExceptionMailThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @Value("${config.mail.devAcc}")
    private String decAcc;
    @Value("${config.mail.opAcc}")
    private String opAcc;

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
//        log.warn(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "() "
//                + e.getMsg() + (e.getData() == null ? "" : e.getData().toString()));
        log.warn(new StringBuilder().append("业务异常 ").append(e.getMsg()).append(" 抛出于 ").append(stackTraceElement.getClassName())
                .append(".").append(stackTraceElement.getMethodName()).append("() ").append(e.getData() == null ? "" : "Data: " + e.getData()).toString());
        return new Result(e.getCode(), null, e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Result doException(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        String info = buffer.toString();
        new ExceptionMailThread(decAcc, "未知错误", String.format("""
                <h1>未知错误</h1>
                <p>错误内容:</p>
                <div><code>%s</code></div>
                """, info)).start();
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        log.error(
                new StringBuilder().append("异常 ").append(e.getMessage()).append(" 抛出于 ")
                        .append(stackTraceElement.getClassName()).append(".").append(stackTraceElement.getMethodName())
                        .append("() 位于行").append(stackTraceElement.getLineNumber()).toString());
        return new Result(Code.UNKNOWN_ERR, null, "系统繁忙,请稍后再试或尝试联系管理员");
    }
}
