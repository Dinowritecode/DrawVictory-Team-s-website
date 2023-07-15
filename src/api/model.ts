export interface RegUser {
    [index: string]: string;

    /** 用户名 */
    username: string,
    /** 用户密码 */
    password: string
    /** 用户邮箱 */
    email: string
}

export class User {
    private _uid: number;
    private _username: string;
    private _email: string;
    private _roles: string[];
    private _permissions: string[];

    public get uid() {
        return this._uid;
    }

    public set uid(uid: number) {
        this._uid = uid;
    }

    public get username() {
        return this._username;
    }

    public set username(username: string) {
        this._username = username;
    }

    public get email() {
        return this._email;
    }

    public set email(email: string) {
        this._email = email;
    }

    public get roles() {
        return this._roles;
    }

    public set roles(roles: string[]) {
        this._roles = roles;
    }

    public get permissions() {
        return this._permissions;
    }

    public set permissions(permissions: string[]) {
        this._permissions = permissions;
    }

    constructor(uid: number, username: string, email: string, roles: string[], permissions: string[]) {
        this._uid = uid;
        this._username = username;
        this._email = email;
        this._roles = roles;
        this._permissions = permissions;
    }
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
    REGISTER_ERR = 1101,//基本注册错误
    REGISTER_EXIST_USERNAME_ERR = 1102,//用户名已存在
    REGISTER_EXIST_EMAIL_ERR = 1103,//邮箱已存在
    REGISTER_EMAIL_FORMAT_ERR = 1104,//邮箱格式错误
    REGISTER_USERNAME_FORMAT_ERR = 1105,//用户名格式错误
    REGISTER_PASSWORD_FORMAT_ERR = 1106,//密码格式错误
    REGISTER_EMPTY_DATA_ERR = 1107,//空数据(信息未填写完全)
    REGISTER_VERIFICATION_CODE_ERR = 1108,//验证码错误
    REGISTER_VERIFY_WITHOUT_PRE_REQUEST_ERR = 1109,//未预先请求验证码直接发送注册请求
    REGISTER_UNKNOWN_ERR = 1199,//未知错误
    UPDATE_ERR = 2101,
    DELETE_ERR = 3101,
    GET_ERR = 4101,
    GET_USERNAME_NOT_EXIST_ERR = 4102,
    GET_ID_NOT_EXIST_ERR = 4103,
    ACTIVATE_ERR = 5001,
    UNKNOWN_ERR = 9999,
    AUTO_LOGIN_ERR = 6002,
    LOGIN_ERR = 6003
}