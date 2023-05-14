package com.evencyan.controller;

public class Code {
    public static final int REGISTER_OK = 1000;
    public static final int UPDATE_OK = 2000;
    public static final int DELETE_OK = 3000;
    public static final int GET_OK = 4000;
    public static final int ACTIVATE_OK = 5000;
    public static final int AUTO_LOGIN_OK = 6000;
    public static final int LOGIN_OK = 6001;

    public static final int REGISTER_ERR = 1101;//基本注册错误
    public static final int REGISTER_EXIST_USERNAME_ERR = 1102;//用户名已存在
    public static final int REGISTER_EXIST_EMAIL_ERR = 1103;//邮箱已存在
    public static final int REGISTER_EMAIL_FORMAT_ERR = 1104;//邮箱格式错误
    public static final int REGISTER_USERNAME_FORMAT_ERR = 1105;//用户名格式错误
    public static final int REGISTER_PASSWORD_FORMAT_ERR = 1106;//密码格式错误
    public static final int REGISTER_EMPTY_DATA_ERR = 1107;//空数据(信息未填写完全)
    public static final int REGISTER_VERIFICATION_CODE_ERR = 1108;//验证码错误
    public static final int REGISTER_VERIFY_WITHOUT_PRE_REQUEST_ERR = 1109;//未预先请求验证码直接发送注册请求(同一验证码多次请求或技术手段直接请求)
    public static final int REGISTER_UNKNOWN_ERR = 1199;//未知错误

    public static final int UPDATE_ERR = 2101;
    public static final int DELETE_ERR = 3101;
    public static final int GET_ERR = 4101;
    public static final int GET_USERNAME_NOT_EXIST_ERR = 4102;
    public static final int GET_ID_NOT_EXIST_ERR = 4103;
    public static final int ACTIVATE_ERR = 5001;
    public static final int UNKNOWN_ERR = 9999;

    public static final int AUTO_LOGIN_ERR = 6002;
    public static final int LOGIN_ERR = 6003;



    public static final int LOG_TYPE_UNKNOWN = 1;
    public static final int LOG_TYPE_BUSINESS = 2;
}
