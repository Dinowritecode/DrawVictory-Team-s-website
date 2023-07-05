CREATE DATABASE IF NOT EXISTS dvt;
USE dvt;
-- 删除表
DROP TABLE IF EXISTS sys_user, sys_role, sys_permission, ref_user_role, ref_role_permission;

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`          INT AUTO_INCREMENT COMMENT '主键id',
    `name`        VARCHAR(51)  NOT NULL UNIQUE COMMENT '角色名',
    `description` VARCHAR(100) NOT NULL COMMENT '角色描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT '角色表';
-- 添加默认角色
INSERT INTO sys_role (id, name, description)
VALUES (100, 'super_admin', '超级管理员'),
       (200, 'admin', '管理员'),
       (300, 'user', '普通用户');

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user`
(
    `uid`           INT AUTO_INCREMENT COMMENT '主键id',
    `username`      VARCHAR(20)  NOT NULL COMMENT '用户名',
    `password`      CHAR(60)     NOT NULL COMMENT '密码(SHA-256)',
    `email`         VARCHAR(100) NOT NULL COMMENT '邮箱',
    `register_time` INT          NULL     DEFAULT NULL COMMENT '注册时间戳',
    `avatar`        BLOB         NULL     DEFAULT NULL COMMENT '头像',
    `status`        TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态(0-停用 1-正常)',
    `is_deleted`    int          NOT NULL DEFAULT 0 COMMENT '是否删除[0-正常 非0(=uid)-删除]',
    `version`       INT(11)      NOT NULL DEFAULT 1 COMMENT '乐观锁字段',
    PRIMARY KEY (`uid`),
    UNIQUE KEY `uk_username` (`username`, `is_deleted`) USING BTREE,
    UNIQUE KEY `uk_email` (`email`, `is_deleted`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1 COMMENT '用户表';
-- 自动将register_time字段设置为当前时间戳
CREATE TRIGGER sys_user_register_time
    BEFORE INSERT
    ON sys_user
    FOR EACH ROW
BEGIN
    SET NEW.register_time = UNIX_TIMESTAMP();
END;

-- 权限表
CREATE TABLE IF NOT EXISTS `sys_permission`
(
    `id`          int                                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(50)                                    NOT NULL COMMENT '权限名称',
    `description` varchar(100) DEFAULT NULL COMMENT '权限描述',
    `type`        ENUM ('user', 'content', 'management', 'data') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';
-- 权限
INSERT INTO sys_permission (id, name, description, type)
VALUES (1, 'login', '用户登录权限', 'user'),
       (2, 'register', '用户注册权限', 'user'),
       (3, 'view_article', '查看文章权限', 'content'),
       (4, 'download_file', '下载文件权限', 'content'),
       (5, 'add_user', '添加用户权限', 'management'),
       (6, 'edit_user', '修改用户权限', 'management'),
       (7, 'delete_user', '删除用户权限', 'management'),
       (8, 'view_data', '查看数据权限', 'data'),
       (9, 'edit_data', '修改数据权限', 'data'),
       (10, 'delete_data', '删除数据权限', 'data');

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `ref_user_role`
(
    `id`      int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` int NOT NULL COMMENT '用户ID',
    `role_id` int NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE,
    CONSTRAINT `fk_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `ref_role_permission`
(
    `id`            int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id`       int NOT NULL COMMENT '角色ID',
    `permission_id` int NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE,
    KEY `idx_permission_id` (`permission_id`) USING BTREE,
    CONSTRAINT `fk_role_permission_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_role_permission_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色权限关联表';
-- 向role_permission表中插入超级管理员权限记录
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 1);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 2);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 3);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 4);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 5);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 6);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 7);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 8);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 9);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (100, 10);

-- 向role_permission表中插入管理员权限记录
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 1);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 2);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 3);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 4);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 5);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 6);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (200, 7);

-- 向role_permission表中插入普通用户权限记录
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (300, 1);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (300, 2);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (300, 3);
INSERT INTO ref_role_permission (role_id, permission_id)
VALUES (300, 4);


-- 添加默认超级管理员账户(密码为qo3G89jI5Bqi3W)
INSERT INTO sys_user (uid, username, password, email)
VALUES (1, 'super_admin', '$2a$10$BVgEje1WboYtyh1Okev4HeTjsPKtnaO0i254zpQYq8r5fu0K002FO', 'example@example.com');
INSERT INTO ref_user_role(user_id, role_id)
VALUES (1, 100),
       (1, 200),
       (1, 300);