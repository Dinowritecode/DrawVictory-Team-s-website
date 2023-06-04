package com.evencyan.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("s_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    @TableField("register_time")
    private Integer registerTime;
    @JsonIgnore
    private Boolean status;
    @TableField(value = "is_deleted", select = false)
    @TableLogic
    @JsonIgnore
    private Boolean isDeleted;
    @TableField(select = false)
    private String password;
    private String username, email, role;
    private Byte[] avatar;
    @Version
    @TableField(select = false)
    private Integer version;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public PendingUser toPendingUser() {
        return new PendingUser(this);
    }
}
