export interface User {
    /** 用户id */
    id: number,
    /** 用户名 */
    username: string,
    /** 用户头像url */
    avatar: string
}

/**
 * @param T data数据类型
 */
export interface Response<T = null> {
    /** 状态码 */
    code: number,
    /** 数据 */
    data: T | null,
    /** 信息 */
    msg: string | null
}

// 如果 spring 错误，返回的对象格式
export interface SpringError {
    timestamp: string,
    status: number,
    error: string,
    msg: string,
    path: string
}

// 如果 spring 成功，返回 list 情况
export interface SpringList<T> {
    data: T[],
    msg?: string,
    code: number
}

// 如果 spring 成功，返回 page 情况
export interface SpringPage<T> {
    code: number,
    data: { list: T[], total: number },
    msg?: string
}

export interface SpringObject<T> {
    code: number,
    data: T,
    /** 提示信息 */
    msg: string
}

// 如果 spring 成功，返回 string 情况
export interface SpringString {
    data: string,
    msg?: string,
    code: number
}

import {AxiosResponse} from "axios";

export interface AxiosRespError extends AxiosResponse<SpringError> {
}

export interface AxiosRespList<T> extends AxiosResponse<SpringList<T>> {
}

export interface AxiosRespPage<T> extends AxiosResponse<SpringPage<T>> {
}

export interface AxiosRespString extends AxiosResponse<SpringString> {
}

export interface AxiosRespObject<T> extends AxiosResponse<SpringObject<T>> {
}

export enum Code {
    REGISTER_OK = 1000,
    UPDATE_OK = 2000,
    DELETE_OK = 3000,
    GET_OK = 4000,
    ACTIVE_OK = 5000,
    AUTO_LOGIN_OK = 6000,
    LOGIN_OK = 6001,
    REGISTER_ERR = 1101,//基本注册错误
    REGISTER_EXIST_USERNAME_ERR = 1102,//用户名已存在
    REGISTER_EXIST_EMAIL_ERR = 1103,//邮箱已存在
    REGISTER_EMAIL_FORMAT_ERR = 1104,//邮箱格式错误
    REGISTER_USERNAME_FORMAT_ERR = 1105,//用户名格式错误
    REGISTER_PASSWORD_FORMAT_ERR = 1106,//密码格式错误
    REGISTER_EMPTY_DATA_ERR = 1107,//空数据(信息未填写完全)
    REGISTER_VERIFICATION_CODE_ERR = 1108,//验证码错误
    REGISTER_NO_REQUEST_BEFORE_VERIFY_ERR = 1109,//未预先请求验证码直接发送注册请求
    REGISTER_UNKNOWN_ERR = 1199,//未知错误
    UPDATE_ERR = 2101,
    DELETE_ERR = 3101,
    GET_ERR = 4101,
    GET_USERNAME_NOT_EXIST_ERR = 4102,
    GET_ID_NOT_EXIST_ERR = 4103,
    ACTIVE_ERR = 5001,
    UNKNOWN_ERR = 9999,
    AUTO_LOGIN_ERR = 6002,
    LOGIN_ERR = 6003
}

export const SpringObjectError: SpringObject<any> = {
    code: Code.UNKNOWN_ERR,
    data: null,
    msg: '系统繁忙,请稍后再试'
}