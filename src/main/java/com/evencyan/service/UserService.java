package com.evencyan.service;

import com.evencyan.controller.Result;
import com.evencyan.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {
    /**
     * 根据id查询用户
     *
     * @param id 用户id
     */
    Result getResultById(int id);

    User getUserById(int id);

    /**
     * 根据username查询用户
     *
     * @param username 用户名
     */
    Result getResultByUsername(String username);


    /**
     * 根据激活码激活用户
     *
     * @param token 激活码
     */
    Result activateUser(String token);

    /**
     * 注册用户
     *
     * @param user
     */
    Result register(User user);

//    Result login(User user, HttpSession session,HttpServletResponse response,boolean autoLogin);
}
