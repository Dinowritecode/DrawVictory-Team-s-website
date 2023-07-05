package com.evencyan.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDAO{
    @Select("""
            SELECT ur.`role_id` FROM `sys_user` u
                LEFT JOIN `ref_user_role` ur ON u.`uid` = ur.user_id
            WHERE u.`uid` = #{uid}
                AND u.`status` = 1
                AND u.`is_deleted` = 0
            """)
    List<Integer> selectRolesByUid(Integer uid);
}
