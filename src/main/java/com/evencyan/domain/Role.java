package com.evencyan.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
@TableName("sys_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name,description;

    @JsonValue
    public Integer getId(){
        return id;
    }

    @JsonCreator
    public Role(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}