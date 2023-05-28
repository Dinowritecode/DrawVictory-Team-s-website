package com.evencyan.service;

import com.evencyan.controller.Result;
import com.evencyan.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {


    /**
     * 根据激活码激活用户
     *
     * @param token 激活码
     */
    Result activate(String token);

    /**
     * 注册用户
     */
    Result register(User user);

    Result setAvatar(User user);

//    Result login(User user, HttpSession session,HttpServletResponse response,boolean autoLogin);
}
