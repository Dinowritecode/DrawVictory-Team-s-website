package com.evencyan.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ref_user_role")
public class User2Role {
    private Integer id, userId, roleId;
}
