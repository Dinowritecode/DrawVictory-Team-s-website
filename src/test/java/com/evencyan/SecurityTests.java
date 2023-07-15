package com.evencyan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecurityTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Test
    void passwordEncoderTest() {
//        String s1 = passwordEncoder.encode("qo3G89jI5Bqi3W");
//        String s2 = passwordEncoder.encode("test1234");
//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s1.length());
//        System.out.println(s2.length());
        System.out.println(passwordEncoder.matches("qo3G89jI5Bqi3W", "$2a$10$BVgEje1WboYtyh1Okev4HeTjsPKtnaO0i254zpQYq8r5fu0K002FO"));
    }

    @Test
    void testUserDetailService() {
//        UserDetails loginUser = userDetailsService.loadUserByUsername("super_admin");
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("super_admin", "qo3G89jI5Bqi3W"));
        System.out.println(authenticate);
    }

    void testTime() {
        //测试用时
    }
}
