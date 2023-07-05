package com.evencyan.service.impl;

import com.alibaba.fastjson2.JSON;
import com.evencyan.controller.Code;
import com.evencyan.controller.Result;
import com.evencyan.domain.LoginUser;
import com.evencyan.domain.User;
import com.evencyan.exception.BusinessException;
import com.evencyan.service.AuthService;
import com.evencyan.util.JwtUtil;
import com.evencyan.util.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RedisCache redisCache;

    @Override
    public Result login(User user, HttpServletResponse response) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authenticate == null) throw new BusinessException(Code.LOGIN_ERR, null, "登录失败");
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String uuid = JwtUtil.getUUID();
        loginUser.setTokenUUID(uuid);
        String token = JwtUtil.createJWT(uuid, JSON.toJSONString(loginUser.getUser()), JwtUtil.JWT_TTL);
        response.setHeader("Authorization", "Bearer " + token);
        redisCache.setCacheObject("token:" + uuid, loginUser, JwtUtil.JWT_TTL.intValue(), TimeUnit.MILLISECONDS);
        return Result.success(Code.LOGIN_OK, null, "登录成功");
    }

    @Override
    public Result logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        redisCache.deleteObject("token:" + loginUser.getTokenUUID());
        return Result.success(Code.LOGOUT_OK, null, "注销成功");
    }
}