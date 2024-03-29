package com.evencyan.controller;

import com.evencyan.domain.User;
import com.evencyan.exception.BusinessException;
import com.evencyan.service.UserService;
import com.evencyan.util.CheckCodeUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/register")//用户注册
    public Result register(@RequestBody User user, @RequestParam("verificationCode") String verificationCode, @RequestParam("verificationId") String verificationId) {
        if (StringUtils.isEmpty(verificationCode) || StringUtils.isEmpty(verificationId))//验证码或验证id为空
            throw new BusinessException(Code.REGISTER_EMPTY_DATA_ERR, user, "请求参数不齐全");
        String verificationCodeInRedis = redisTemplate.opsForValue().getAndDelete("verId:" + verificationId);//获取并销毁验证码
        if (StringUtils.isEmpty(verificationCodeInRedis))//验证码是否为空
            throw new BusinessException(Code.REGISTER_VERIFICATION_CODE_EXPIRED_ERR, user, "您的验证码不存在或已过期，请重试");
        if (!verificationCode.equalsIgnoreCase(verificationCodeInRedis))
            throw new BusinessException(Code.REGISTER_VERIFICATION_CODE_ERR, user, "验证码错误");
        return userService.register(user);
    }

    @PostMapping("/activate/{token}")
    public Result activate(@PathVariable String token) {
        return userService.activate(token.trim());
    }

    @GetMapping("/verify")
    public void verify(HttpServletResponse response) throws IOException {
        String verificationId = RandomStringUtils.randomAlphanumeric(10);//生成随机验证id
        response.setHeader("Verification-Id", verificationId);
        String verificationCode = CheckCodeUtil.outputVerifyImage(100, 50, response.getOutputStream(), 4);
        redisTemplate.opsForValue().set("verId:" + verificationId, verificationCode, 5, TimeUnit.MINUTES);//验证码5分钟内有效
        log.info("verification code: " + verificationCode);
    }

    @PutMapping("/avatar")
    public Result upload(User user) {
        return userService.setAvatar(user);
    }

    @GetMapping("/{uid}")
    @PreAuthorize("@security.hasPermission('user:view_user')")
    public Result getUser(@PathVariable Integer uid, @Nullable @RequestParam List<String> fields) {
        return userService.getUser(uid, fields);
    }

    @RequestMapping("/test")
    @PreAuthorize("@security.hasPermission('view_user')")
    public Result test() {
        return Result.success(Code.GET_OK, "ok", null);
    }

//    public Result
}