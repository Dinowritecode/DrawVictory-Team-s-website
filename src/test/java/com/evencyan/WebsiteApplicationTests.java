package com.evencyan;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.evencyan.dao.UserDao;
import com.evencyan.dao.PendingUserDao;
import com.evencyan.domain.User;
import com.evencyan.domain.PendingUser;
import com.evencyan.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
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

    public static String toJson(Object object) throws Exception {
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
}
