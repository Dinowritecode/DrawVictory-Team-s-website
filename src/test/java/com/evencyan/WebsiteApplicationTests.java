package com.evencyan;


import com.evencyan.dao.PendingUserDAO;
import com.evencyan.dao.UserDAO;
import com.evencyan.domain.LoginUser;
import com.evencyan.domain.PendingUser;
import com.evencyan.domain.User;
import com.evencyan.service.impl.MailServiceImpl;
import com.evencyan.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootTest
@Slf4j
class WebsiteApplicationTests {
    @Autowired
    private UserDAO userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PendingUserDAO pendingUserDao;
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    void contextLoads() {
        User user = new User("abc","def","email");
        redisTemplate.opsForValue().set("test", user);
        User redisUser = (User) redisTemplate.opsForValue().get("test");
        System.out.println(redisUser);
    }

    @Test
    void test2() throws Exception {
    }

    @Autowired
    private Random random;

    @Test
    void userDaoTest() throws Exception {
//        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(User::getEmail, "example@example.com");
//        lqw.select(User::getUid,User::getEmail,User::getUsername);
//        User user = userDao.selectOne(lqw);
//        System.out.println(toJson(user));
//        System.out.println(userDao.selectById(1));
//        User user = new User("abcde","123","tesaat@abc.com");
//        user.setStatus(false);
//        userDao.insert(user);
//        User user = new User("testuser", "abcd1123", "abcawd@qq.wa");
//        userService.register(user);
        mailService.sendActivationMail("激活","undefined@evencyan.onmicrosoft.com","user","baidu.com");
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
