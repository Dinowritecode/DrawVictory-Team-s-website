create database if not exists dvt;
use dvt;
create table if not exists s_user(
    `id` int auto_increment comment 'id',
    `username` varchar(20) not null unique comment '用户名',
    `password` varchar(20) not null comment '密码(SHA-1)',
    `email` varchar(60) not null comment '邮箱',
    `register_time` int not null comment '注册时间戳',
    `avatar` varchar(100) default null comment '头像url',
    `status` char(1) not null default '1' comment '状态(0停用 1正常)',
    `role` tinyint not null comment '角色',
    `is_delete` char(1) not null default '0' comment '是否删除(0正常 1删除)',
    primary key (`id`)
) engine=InnoDB DEFAULT CHARSET=utf8;