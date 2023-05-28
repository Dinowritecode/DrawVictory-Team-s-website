package com.evencyan;


import com.evencyan.dao.UserDao;
import com.evencyan.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@SpringBootTest
@Slf4j
class WebsiteApplicationTests {
    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
        System.out.println(DigestUtils.sha256Hex("qo3G89jI5Bqi3W"));
    }

    @Autowired
    private Random random;
    //随机加盐方法
    String addRandomSalt() {
        //生成随机盐

        String salt = "";
        for (int i = 0; i < 6; i++) {
            salt += (char) (random.nextInt(26) + 97);
        }
        return salt;
    }

    @Test
    void userDaoTest(){
//        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(User::getEmail,"example@example.com");
//        User user = userDao.selectOne(lqw);
//        System.out.println(user);
//        System.out.println(userDao.selectById(1));
//        User user = new User("abcde","123","tesaat@abc.com");
//        user.setStatus(false);
//        userDao.insert(user);
    }

}
