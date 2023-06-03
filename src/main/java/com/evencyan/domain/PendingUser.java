package com.evencyan.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "pending",timeToLive = 60*60)
@Data
@AllArgsConstructor
public class PendingUser {
    @Id
    private Integer uid;
    @Indexed
    private String username;
    @Indexed
    private String email;
    private String password;

    public User toUser(){
        return new User(this.username,this.password,this.email);
    }
}
