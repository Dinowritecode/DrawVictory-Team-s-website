package com.evencyan.controller;

import com.evencyan.domain.User;
import com.evencyan.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletResponse response) {
        return authService.login(user, response);
    }

    @PostMapping("/logout")
    public Result logout() {
        return authService.logout();
    }
}
