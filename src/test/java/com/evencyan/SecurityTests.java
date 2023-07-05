package com.evencyan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecurityTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoderTest() {
//        String s1 = passwordEncoder.encode("qo3G89jI5Bqi3W");
//        String s2 = passwordEncoder.encode("test1234");
//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s1.length());
//        System.out.println(s2.length());
        System.out.println(passwordEncoder.matches("qo3G89jI5Bqi3W", "$2a$10$jK.eGrqL7k2dmuQNOaI1AuYc6zHWb6NB1xYeCrpxTCkeq5kX3YuGa"));
    }
}
