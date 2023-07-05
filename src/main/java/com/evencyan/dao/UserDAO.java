package com.evencyan.dao;

import com.evencyan.domain.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO extends MPJBaseMapper<User> {
    @Select("""
            SELECT * FROM sys_user u
            WHERE u.uid = #{uid}
                AND u.status = 1
                AND u.is_deleted = 0
            """)
    @Results({
            @Result(id = true, property = "uid", column = "uid")
    })
    User selectWithRoleByUid(@Param("uid") Integer uid);
}
