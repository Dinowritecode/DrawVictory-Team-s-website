package com.evencyan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evencyan.domain.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao extends BaseMapper<Role> {
}
