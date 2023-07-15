package com.evencyan.controller;

import com.evencyan.exception.BusinessException;
import com.evencyan.exception.SystemException;
import com.evencyan.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionAdvice {
    @Value("${config.mail.devAcc}")
    private String decAcc;
    @Value("${config.mail.opsAcc}")
    private String opAcc;
    private final MailService mailService;

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        log.warn("业务异常 {} 抛出于 {}.{}(){}",
                e.getMsg(), stackTraceElement.getClassName(), stackTraceElement.getMethodName(),
                e.getData() == null ? "" : " Data: " + e.getData());
        return Result.failure(e.getCode(), null, e.getMsg());
    }

    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException e) {
        sendMail("系统异常", decAcc, e);
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        e.printStackTrace();
        log.error("异常 {} 抛出于 {}.{}(){}", e.getMsg(), stackTraceElement.getClassName(),
                stackTraceElement.getMethodName(), (e.getData() == null ? "" : " Data: " + e.getData()));
        return Result.failure(e.getCode(), null, e.getMsg());
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result doAuthenticationException(AuthenticationException e) {
        return Result.failure(Code.AUTH_ERR, null, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result doAccessDeniedException(AccessDeniedException e) {
        return Result.failure(Code.AUTH_ERR, null, "您无权访问此资源");
    }

    @ExceptionHandler(Exception.class)
    public Result doException(Exception e) {
        sendMail("未知异常", decAcc, e);
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        e.printStackTrace();
        log.error("异常 {} 抛出于 {}.{}() 位于行{}", e.getMessage(), stackTraceElement.getClassName(),
                stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
        return Result.failure(Code.UNKNOWN_ERR, null, "系统繁忙,请稍后再试或尝试联系管理员");
    }

    private void sendMail(String type, String to, Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        String info = buffer.toString();
        mailService.sendExceptionMail(type, to, info);
    }
}
