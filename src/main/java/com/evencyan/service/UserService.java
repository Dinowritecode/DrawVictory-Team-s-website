package com.evencyan.service;

import com.evencyan.controller.Result;
import com.evencyan.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {


    /**
     * 根据激活码激活用户
     *
     * @param token 激活码(待激活用户于redis中的uid)
     */
    Result activate(String token);

    /**
     * 注册用户
     */
    Result register(User user);

    Result setAvatar(User user);

    Result getUser(Integer uid, List<String> fields);
}
