package com.evencyan;

import com.evencyan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private UserService userService;

    @Test
    void testGetUserInfo() {
        System.out.println(userService.getUser(1, List.of("username", "password", "email")));
    }
}
