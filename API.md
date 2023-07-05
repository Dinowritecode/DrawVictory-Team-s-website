# API文档

若无特别注明，所有响应都是以下格式的JSON

| 参数名  | 类型     | 描述             |
|------|--------|----------------|
| code | int    | 状态码            |
| data | object | 具体相应数据(可为null) |
| msg  | string | 提示信息(可为null)   |

## 用户相关接口

### **GET** /users/verify

注册用户验证码

#### 请求参数

无

#### 响应

响应一个验证码图片数据(byte[])

### 响应头

| 属性名             | 属性值             |
|-----------------|-----------------|
| Verification-Id | 验证ID(用于注册时识别用户) |

#### 响应示例

![验证码图片](./apiImg/verificationCode.jpg)

### **POST** /users/register

注册新用户，请求前需要先获取验证码，验证码5分钟内有效

#### 请求参数

| 参数名              | 类型     | 描述   | 注释 |
|------------------|--------|------|----|
| verificationCode | string | 验证码  |
| verificationId   | string | 验证id |

#### 请求体

请求体应是一个JSON对象，包含以下属性

| 参数名      | 类型     | 描述   | 注释                                   |
|----------|--------|------|--------------------------------------|
| username | string | 用户名  | 用户名只允许包含数字，大小写字母，下划线和连词线，且长度在4-16位之间 |
| password | string | 密码   | 密码需包含字母，符号或者数字中至少两项，且长度在6-16位之间      |
| email    | string | 用户邮箱 | 正确的邮箱格式                              |

#### 请求示例

##### 请求参数

| Key              | Value      |
|------------------|------------|
| verificationCode | FAYL       | 
| verificationId   | TIPlfBuaqW | 

##### 请求体

```json
{
  "name": "Alice",
  "email": "alice@example.com",
  "password": "example123456"
}
```

#### 响应

| 参数名     | 类型     | 描述   |
|---------|--------|------|
| code    | int    | 状态码  |
| data    | null   | 空    |
| message | string | 提示信息 |

#### 响应示例

```json
{
  "code": 1000,
  "data": null,
  "message": "请查收邮箱并在一小时内完成激活"
}
```

### **GET** /users/:uid

获取指定uid的用户信息

#### 请求参数

无

#### 响应

| 参数名   | 类型     | 描述   |
|-------|--------|------|
| id    | int    | 用户ID |
| name  | string | 用户名  |
| email | string | 用户邮箱 |

#### 响应示例

```json
[
  {
    "id": 1,
    "name": "Alice",
    "email": "alice@example.com"
  },
  {
    "id": 2,
    "name": "Bob",
    "email": "bob@example.com"
  }
]
```
