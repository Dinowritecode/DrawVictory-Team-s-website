package com.evencyan.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer uid;
    private Integer registerTime;
    @JsonIgnore
    private Boolean status;
    @TableField(select = false)
    @TableLogic(delval = "uid")
    private Boolean isDeleted;
    @JsonIgnoreProperties(value = {"password"}, allowSetters = true)
    @JSONField(serialize = false)
    private String password;
    private String username, email;
    private Byte[] avatar;
    @Version
    @TableField(select = false)
    private Integer version;

    @TableField(exist = false)
    private List<Integer> roles;
    @TableField(exist = false)
    private List<Integer> permissions;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public PendingUser toPendingUser() {
        return new PendingUser(this);
    }
}

