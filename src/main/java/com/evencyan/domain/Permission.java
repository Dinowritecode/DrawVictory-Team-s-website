package com.evencyan.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
@TableName("sys_permission")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name, description, type;

    public static class Type {
        public static final String USER = "user";
        public static final String CONTENT = "content";
        public static final String MANAGEMENT = "management";
        public static final String DATA = "data";
    }

    @JsonCreator
    public Permission(Integer id) {
        this.id = id;
    }

    @JsonValue
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}