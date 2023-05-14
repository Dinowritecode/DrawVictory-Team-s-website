package com.evencyan.dao;

import com.evencyan.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    @Select("select * from s_user where id = #{id}")
    User getById(Integer id);

    @Select("select * from s_user where username = #{username}")
    User getByUsername(String username);

    @Select("select * from s_user where email = #{email}")
    User getByEmail(String email);

    @Select("select * from s_user where token = #{token} and status = 0")
    User getByToken(String token);

    @Insert("insert into s_user (username, password, email, token, reg_time) values (#{username}, #{password}, #{email}, #{token}, unix_timestamp())")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void register(User user);

    @Delete("delete from s_user where id = #{id}")
    void delete(User user);


    @Delete("delete from s_user where id in(select id from (select id from s_user where status = 0 and unix_timestamp() > reg_time + 3600) invalid)")
    void deleteInvalid();

    @Update("update s_user set status = 1,reg_time = unix_timestamp() where id = #{id}")
    int activate(User user);
}
