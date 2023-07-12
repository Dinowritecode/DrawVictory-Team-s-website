package com.evencyan.dao;

import com.evencyan.domain.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO extends MPJBaseMapper<User> {
}
