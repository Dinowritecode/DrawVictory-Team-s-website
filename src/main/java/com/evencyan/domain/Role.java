package com.evencyan.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class Role {
    @TableId(type = IdType.NONE)
    private Integer rid;
    @TableField("role_name")
    private String name;
    @TableField("role_desc")
    private String description;
}
