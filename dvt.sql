create database if not exists dvt;
use dvt;
-- 删除表
drop table if exists s_user;
drop table if exists s_role;
-- 创建角色表
create table if not exists s_role
(
    `rid`       int auto_increment comment 'role id',
    `role_name` varchar(20)  not null unique comment '角色名',
    `role_desc` varchar(100) not null comment '角色描述',
    `status`    char(1)      not null default '1' comment '状态(0停用 1正常)',
    `is_delete` char(1)      not null default '0' comment '是否删除(0正常 1删除)',
    primary key (`rid`)
) engine = InnoDB
  default charset = utf8;
-- 添加默认角色
insert into s_role (rid, role_name, role_desc)
values (100, 'user', '普通用户'),
       (101, 'admin', '管理员'),
       (102, 'super_admin', '超级管理员');
-- 创建用户表
create table if not exists s_user
(
    `uid`           int auto_increment comment 'id',
    `username`      varchar(20)  not null unique comment '用户名',
    `password`      varchar(20)  not null comment '密码(SHA-256)',
    `email`         varchar(100) not null comment '邮箱',
    `register_time` int          null     default null comment '注册时间戳',
    `avatar`        varchar(100)          default null comment '头像url',
    `status`        char(1)      not null default '1' comment '状态(0停用 1正常)',
    `role`          int          not null default 100 comment '角色',
    `is_delete`     char(1)      not null default '0' comment '是否删除(0正常 1删除)',
    primary key (`uid`),
    foreign key (`role`) references s_role (`rid`)
) engine = InnoDB
  default charset = utf8;
-- 自动将register_time字段设置为当前时间戳
create trigger s_user_register_time
    before insert
    on s_user
    for each row
begin
    set new.register_time = unix_timestamp();
end;
-- 添加默认超级管理员账户
insert into s_user (username, password, email, role)
values ('super_admin', 'qo3G89jI5Bqi3W', 'example@example.com', 102);