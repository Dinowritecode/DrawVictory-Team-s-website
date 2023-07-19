export interface UserType {
    uid?: bigint
    username?: string
    email?: string
    roles?: string[]
    permissions?: string[]
}

export class User implements UserType{
    uid?: bigint;
    username?: string;
    email?: string;
    roles?: string[];
    permissions?: string[];
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
    msg?: string,
    /** 是否成功 */
    success: boolean;
}

export enum Code {
    REGISTER_OK = 1000,
    UPDATE_OK = 2000,
    DELETE_OK = 3000,
    GET_OK = 4000,
    ACTIVATE_OK = 5000,
    AUTO_LOGIN_OK = 6000,
    LOGIN_OK = 6001,
    REGISTER_ERR = 1101,
    REGISTER_EXIST_USERNAME_ERR = 1102,
    REGISTER_EXIST_EMAIL_ERR = 1103,
    REGISTER_EMAIL_FORMAT_ERR = 1104,
    REGISTER_USERNAME_FORMAT_ERR = 1105,
    REGISTER_PASSWORD_FORMAT_ERR = 1106,
    REGISTER_EMPTY_DATA_ERR = 1107,
    REGISTER_VERIFICATION_CODE_ERR = 1108,
    REGISTER_VERIFICATION_CODE_EXPIRED_ERR = 1109,
    REGISTER_PENDING_ACTIVATION_ERR = 1110,
    REGISTER_UNKNOWN_ERR = 1199,
    UPDATE_ERR = 2101,
    DELETE_ERR = 3101,
    GET_ERR = 4101,
    GET_USERNAME_NOT_EXIST_ERR = 4102,
    GET_ID_NOT_EXIST_ERR = 4103,
    ACTIVATE_ERR = 5001,
    UNKNOWN_ERR = 9999,
    AUTO_LOGIN_ERR = 6002,
    LOGIN_ERR = 6003,
    LOGIN_USERNAME_OR_PASSWORD_ERROR = 6004,
    LOGOUT_OK = 6005,
    LOGOUT_ERR = 6006,
    TOKEN_ILLEGAL_ERR = 7001,
    TOKEN_EXPIRED_ERR = 7002,
    AUTH_ERR = 8001,
    AUTH_ANONYMOUS_ERR = 8002,
    ACCESS_DENIED_ERR = 9001,
}