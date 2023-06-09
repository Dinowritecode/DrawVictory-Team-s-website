package com.evencyan;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.evencyan.dao.UserDao;
import com.evencyan.dao.PendingUserDao;
import com.evencyan.domain.User;
import com.evencyan.domain.PendingUser;
import com.evencyan.service.impl.MailServiceImpl;
import com.evencyan.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;

@SpringBootTest
@Slf4j
class WebsiteApplicationTests {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PendingUserDao pendingUserDao;

    @Test
    void contextLoads() {
        userDao.insert(new User("testuser", DigestUtils.sha256Hex("114514"),"email"));
    }

    @Autowired
    private Random random;
    @Test
    void userDaoTest() throws Exception {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, "example@example.com");
//        lqw.select(User::getUid,User::getEmail,User::getUsername);
        User user = userDao.selectOne(lqw);
        System.out.println(toJson(user));
//        System.out.println(userDao.selectById(1));
//        User user = new User("abcde","123","tesaat@abc.com");
//        user.setStatus(false);
//        userDao.insert(user);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void redisTest() {
//        System.out.println(pendingUserDao.findByUid(-295861076));
//        System.out.println(pendingUserDao.findById("-295861076"));
//        pendingUserDao.findAll().forEach(System.out::println);
//        redisTemplate.expire("pending:1925406907",20,java.util.concurrent.TimeUnit.SECONDS);
    }

    @Test
    void redisTest2() {
        User user = new User("abc", "123", "test@test.com");
        PendingUser uo = user.toPendingUser();
        System.out.println(user);
        uo = pendingUserDao.save(uo);
        System.out.println(uo.getUid());
    }

    @Test
    void redisFindTest() {
        System.out.println(pendingUserDao.findById("164866111"));
    }

    @Test
    void jsonTest() {
        User user = new User("abc", "123", "test@test.com");
    }

    @Autowired
    private MailServiceImpl mailService;

    @Test
    void mailTest() {
        mailService.sendActivationMail("账户激活", "cmjdcyf@qq.com", "test user", "testUrl.com");
        System.out.println("run comp");
    }
}
