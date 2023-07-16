package com.evencyan.dao;

import com.evencyan.domain.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDAO extends MPJBaseMapper<User> {

    @Update("UPDATE sys_user SET status = 0 WHERE uid = #{uid} AND is_deleted = 0")
    int ban(@Param("uid") Integer uid);

    @Update("UPDATE sys_user SET status = 1 WHERE uid = #{uid} AND is_deleted = 0")
    int unban(@Param("uid") Integer uid);

}
