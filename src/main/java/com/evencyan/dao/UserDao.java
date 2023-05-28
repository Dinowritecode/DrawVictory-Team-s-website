package com.evencyan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evencyan.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao extends BaseMapper<User> {

}
