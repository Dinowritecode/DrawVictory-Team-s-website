package com.evencyan.controller;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Code {
    REGISTER_OK(1000),
    UPDATE_OK(2000),
    DELETE_OK(3000),
    GET_OK(4000),
    ACTIVATE_OK(5000),
    AUTO_LOGIN_OK(6000),
    LOGIN_OK(6001),

    REGISTER_ERR(1101),//基本注册错误
    REGISTER_EXIST_USERNAME_ERR(1102),//用户名已存在
    REGISTER_EXIST_EMAIL_ERR(1103),//邮箱已存在
    REGISTER_EMAIL_FORMAT_ERR(1104),//邮箱格式错误
    REGISTER_USERNAME_FORMAT_ERR(1105),//用户名格式错误
    REGISTER_PASSWORD_FORMAT_ERR(1106),//密码格式错误
    REGISTER_EMPTY_DATA_ERR(1107),//空数据(信息未填写完全)
    REGISTER_VERIFICATION_CODE_ERR(1108),//验证码错误
    REGISTER_VERIFICATION_CODE_EXPIRED_ERR(1109),//未预先请求验证码直接发送注册请求(同一验证码多次请求或技术手段直接请求)
    REGISTER_PENDING_ACTIVATION_ERR(1110),//待激活
    REGISTER_UNKNOWN_ERR(1199),//未知错误

    UPDATE_ERR(2101),
    DELETE_ERR(3101),
    GET_ERR(4101),
    GET_USERNAME_NOT_EXIST_ERR(4102),
    GET_ID_NOT_EXIST_ERR(4103),
    ACTIVATE_ERR(5001),
    UNKNOWN_ERR(9999),

    AUTO_LOGIN_ERR(6002),
    LOGIN_ERR(6003),
    LOGIN_USERNAME_OR_PASSWORD_ERROR(6004),

    LOGOUT_OK(6005),
    LOGOUT_ERR(6006),

    TOKEN_ILLEGAL_ERR(7001),
    TOKEN_EXPIRED_ERR(7002),;

    private final int value;

    @JsonValue
    public int getValue() {
        return value;
    }

    Code(int value) {
        this.value = value;
    }
}
