package com.evencyan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionDAO{

    @Select("""
            SELECT DISTINCT rp.`permission_id`
            FROM ref_user_role ur
                LEFT JOIN `sys_role` r ON ur.`role_Id` = r.`id`
                LEFT JOIN `ref_role_permission` rp ON ur.`role_id` = rp.`role_id`
                LEFT JOIN `sys_user` u ON u.`uid` = ur.`user_id`
            WHERE ur.`user_id` = #{uid}
                AND u.`status` = 1
                AND u.`is_deleted` = 0
            """)
    List<Integer> selectPermissionsByUid(Integer uid);
}
