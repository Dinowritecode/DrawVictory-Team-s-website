package com.evencyan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionDAO{
    @Select("""
            SELECT DISTINCT p.`name`
            FROM `sys_user` u
                LEFT JOIN `ref_user_role` ur ON u.`uid` = ur.`user_id`
                LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`
                LEFT JOIN `ref_role_permission` rp ON r.`id` = rp.`role_id`
                LEFT JOIN `sys_permission` p ON rp.`permission_id` = p.`id`
            WHERE u.`uid` = #{uid}
                AND u.`is_deleted` = 0
                AND u.`status` = 1
            """)
    List<String> selectByUid(Integer uid);
}
