create database if not exists dvt;
use dvt;
-- 删除表
drop table if exists sys_user;
drop table if exists sys_role;
-- 创建角色表
create table if not exists sys_role
(
    `rid`       int auto_increment comment 'role id',
    `role_name` varchar(20)  not null unique comment '角色名',
    `role_desc` varchar(100) not null comment '角色描述',
    primary key (`rid`)
) engine = InnoDB
  default charset = utf8;
-- 添加默认角色
insert into sys_role (rid, role_name, role_desc)
values (100, 'user', '普通用户'),
       (101, 'admin', '管理员'),
       (102, 'super_admin', '超级管理员');
-- 创建用户表
create table if not exists sys_user
(
    `uid`           int auto_increment comment 'id',
    `username`      varchar(20)  not null unique comment '用户名',
    `password`      char(64)  not null comment '密码(SHA-256)',
    `email`         varchar(100) not null unique comment '邮箱',
    `register_time` int          null     default null comment '注册时间戳',
    `avatar`        blob                  default null comment '头像',
    `role_id`          int          not null default 100 comment '角色',
    `status`        tinyint(1)   not null default 1 comment '状态(0停用 1正常)',
    `is_deleted`    tinyint(1)   not null default 0 comment '是否删除(0正常 1删除)',
    `version`       int(11)      not null default 1 comment '乐观锁',
    primary key (`uid`),
    foreign key (`role_id`) references sys_role (`rid`)
) engine = InnoDB
  default charset = utf8;
-- 自动将register_time字段设置为当前时间戳
create trigger sys_user_register_time
    before insert
    on sys_user
    for each row
begin
    set new.register_time = unix_timestamp();
end;
-- 添加默认超级管理员账户(密码为qo3G89jI5Bqi3W)
insert into sys_user (username, password, email, role_id)
values ('super_admin', 'b4422bb1006d65e62ab83f7b401c9aeec3a35a4002c66a7213b912bc9df09079', 'example@example.com', 102);
