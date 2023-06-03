package com.evencyan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.evencyan.controller.Code;
import com.evencyan.controller.Result;
import com.evencyan.dao.PendingUserDao;
import com.evencyan.dao.UserDao;
import com.evencyan.domain.PendingUser;
import com.evencyan.domain.User;
import com.evencyan.exception.BusinessException;
import com.evencyan.exception.SystemException;
import com.evencyan.service.UserService;
import com.evencyan.thread.ActivationMailThread;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private Random random;
    @Autowired
    private PendingUserDao pendingUserDao;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${server.domain}")
    private String location;

    @Override
    public Result activate(String token) {
        Optional<PendingUser> optionalUser = pendingUserDao.findById(token);
        if (!optionalUser.isPresent())
            throw new BusinessException(Code.ACTIVATE_ERR, null, "抱歉，您的激活链接无效或激活有效期已过");
        try {
            userDao.insert(optionalUser.get().toUser());
        } catch (Exception e) {
            throw new BusinessException(Code.ACTIVATE_ERR, null, "激活失败，未知原因");
        }
        return new Result(Code.ACTIVATE_OK, null, "账户激活成功！");
    }

    @Override
    public Result register(User user) {
        if (StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getPassword()) ||
                StringUtils.isEmpty(user.getEmail())
        ) throw new BusinessException(Code.REGISTER_EMPTY_DATA_ERR, user, "请正确填写信息");
        user.setUsername(user.getUsername().trim());
        user.setPassword(user.getPassword().trim());
        user.setEmail(user.getEmail().trim().toLowerCase());
        //->用户名只允许包含数字,大小写字母,下划线和连词线,且长度在4-16位之间
        if (!user.getUsername().matches("[\\w-]{4,16}"))
            throw new BusinessException(Code.REGISTER_USERNAME_FORMAT_ERR, user, "用户名格式有误");
        //->密码需包含字母,符号或者数字中至少两项,且长度在6-16位之间
        if (!user.getPassword().matches("(?!\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}"))
            throw new BusinessException(Code.REGISTER_PASSWORD_FORMAT_ERR, user, "密码格式有误");
        if (!user.getEmail().matches("[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}"))
            throw new BusinessException(Code.REGISTER_EMAIL_FORMAT_ERR, user, "邮箱格式有误");
        if (pendingUserDao.findOneByUsername(user.getUsername()) != null ||
                pendingUserDao.findOneByEmail(user.getEmail()) != null)
            throw new BusinessException(Code.REGISTER_PENDING_ACTIVATION_ERR, user, "您的邮箱或用户名已注册过，请前往邮箱激活或一小时后重试注册");
        if (userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername())) != null)
            throw new BusinessException(Code.REGISTER_EXIST_USERNAME_ERR, user, "用户名已存在");
        if (userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail())) != null)
            throw new BusinessException(Code.REGISTER_EXIST_EMAIL_ERR, user, "您的邮箱已注册过");
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        try {
            PendingUser pendingUser = user.toPendingUser();
            pendingUserDao.save(pendingUser);
            new ActivationMailThread(user.getUsername(), user.getEmail(),
                    location + "/activate.html?" + pendingUser.getUid()).start();
            log.info("注册成功 pendingUid:" + pendingUser.getUid());
        } catch (Exception e) {
            throw new SystemException(Code.REGISTER_UNKNOWN_ERR, null, "服务器繁忙,请稍后重试或尝试联系管理员");
        }
        return new Result(Code.REGISTER_OK, null, "请查收邮箱并在一小时内完成激活");
    }

/*    //使用spring security登录
    @Override
    public Result login(User user) {
        if (StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getPassword())
        ) throw new BusinessException(Code.LOGIN_EMPTY_DATA_ERR, user, "请正确填写信息");
        user.setUsername(user.getUsername().trim());
        user.setPassword(user.getPassword().trim());
        User user1 = userDao.getByUsername(user.getUsername());
        if (user1 == null)
            throw new BusinessException(Code.LOGIN_USERNAME_NOT_EXIST_ERR, user, "用户名不存在");
        if (!user1.getPassword().equals(user.getPassword()))
            throw new BusinessException(Code.LOGIN_PASSWORD_ERR, user, "密码错误");
        if (user1.getRegTime() == 0)
            throw new BusinessException(Code.LOGIN_NOT_ACTIVATE_ERR, user, "账户未激活");
        return new Result(Code.LOGIN_OK, user1, "登录成功");
    }*/

    @Override
    public Result setAvatar(User user) {
        try {
            User updateUser = userDao.selectById(user.getUid());
            updateUser.setAvatar(user.getAvatar());
            userDao.updateById(updateUser);
        } catch (Exception e) {
            throw new SystemException(Code.UPDATE_ERR, user, "头像修改失败");
        }
        return new Result(Code.UPDATE_OK, null, "头像修改成功");
    }
}