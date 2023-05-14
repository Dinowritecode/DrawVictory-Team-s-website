package com.evencyan.controller;

import com.evencyan.domain.User;
import com.evencyan.exception.BusinessException;
import com.evencyan.service.UserService;
import com.evencyan.util.CheckCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/?{verificationCode}")
    public Result register(@RequestBody User user, @PathVariable String verificationCode, HttpSession session, HttpServletRequest request) {
        if (StringUtils.isEmpty(verificationCode))
            throw new BusinessException(Code.REGISTER_EMPTY_DATA_ERR, user, "请输入验证码");
        if (session.getAttribute("verCode") == null)
            throw new BusinessException(Code.REGISTER_VERIFY_WITHOUT_PRE_REQUEST_ERR, user, "无预请求的验证");
        if (!verificationCode.equalsIgnoreCase(session.getAttribute("verCode").toString()))
            throw new BusinessException(Code.REGISTER_VERIFICATION_CODE_ERR, user, "验证码错误");
        session.setAttribute("verCode", null);//验证码销毁
        return userService.register(user);
    }

    @GetMapping("/activate/{token}")
    public Result activate(@PathVariable String token) {
        return userService.activateUser(token);
    }

    @GetMapping("/{id}")
    public Result getUser(@PathVariable int id) {
        return new Result(Code.GET_OK, userService.getUserById(id));
    }

    @GetMapping("/verify")
    public void verCode(HttpSession session, HttpServletResponse response) throws IOException {
        String verCode = CheckCodeUtil.outputVerifyImage(100, 50, response.getOutputStream(), 4);
        session.setAttribute("verCode", verCode);
    }


    @RequestMapping("/test")
    public Result test() {
        log.error("log error");
        return new Result(1, null, "abc");
    }
}