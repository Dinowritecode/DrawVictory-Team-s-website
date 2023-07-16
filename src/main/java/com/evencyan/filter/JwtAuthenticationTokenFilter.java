package com.evencyan.filter;

import com.evencyan.controller.Code;
import com.evencyan.domain.LoginUser;
import com.evencyan.exception.BusinessException;
import com.evencyan.util.JwtUtil;
import com.evencyan.util.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(7);
        String redisKey;
        try {//解析token
            Claims claims = JwtUtil.parseJWT(token);
            redisKey = "token:" + claims.getId();
        } catch (Exception e) {
            throw new BusinessException(Code.TOKEN_ILLEGAL_ERR, null, "非法token", e.getCause());
        }
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
//        if (loginUser == null) throw new BusinessException(Code.TOKEN_EXPIRED_ERR, null, "token不存在或已过期");
        if (loginUser == null) {
            filterChain.doFilter(request, response);
            response.setHeader("Authorization", "Bearer ");
            return;
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
