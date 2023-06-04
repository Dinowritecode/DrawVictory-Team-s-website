package com.evencyan.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * 待激活用户
 */
@RedisHash("pending")
@Data
@NoArgsConstructor
public class PendingUser {
    @Id
    private Integer uid;
    @Indexed
    private String username;
    @Indexed
    private String email;
    private String password;
    @TimeToLive
    private int expiration = 3600;

    public User toUser(){
        return new User(this.username,this.password,this.email);
    }


    public PendingUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public PendingUser(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
