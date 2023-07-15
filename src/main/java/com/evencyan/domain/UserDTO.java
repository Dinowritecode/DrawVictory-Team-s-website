package com.evencyan.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@TableName("sys_user")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {
    private Integer uid;
    private String username;
    private String email;
    private Byte[] avatar;

    @TableField(exist = false)
    private List<String> roles;
    @TableField(exist = false)
    private List<String> permissions;
}
