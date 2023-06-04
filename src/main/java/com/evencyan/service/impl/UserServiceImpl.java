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
import com.evencyan.thread.PendingIndexDeleteThread;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PendingUserDao pendingUserDao;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${server.domain}")
    private String location;
    @Value("${config.pending-user-expire}")
    private int pendingUserExpire;// 秒

    @Override
    public Result activate(String token) {
        Optional<PendingUser> optionalUser = pendingUserDao.findById(token);
        if (optionalUser.isEmpty())//redis中无以此为id的待激活用户
            throw new BusinessException(Code.ACTIVATE_ERR, null, "抱歉，您的激活链接无效或激活有效期已过");
        try {
            userDao.insert(optionalUser.get().toUser());
        } catch (Exception e) {
            throw new BusinessException(Code.ACTIVATE_ERR, null, "激活失败，未知原因");
        }
        pendingUserDao.deleteById(token);
        log.info("激活成功 username:"+ optionalUser.get().getUsername());
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
            pendingUser = pendingUserDao.save(pendingUser);
            //删除过期的pendingUser的索引
            redisTemplate.expire("pending:username:" + pendingUser.getUsername(), pendingUserExpire, TimeUnit.SECONDS);
            redisTemplate.expire("pending:email:" + pendingUser.getEmail(), pendingUserExpire, TimeUnit.SECONDS);
            redisTemplate.expire("pending:%s:idx".formatted(pendingUser.getUid()), pendingUserExpire, TimeUnit.SECONDS);
            new PendingIndexDeleteThread(pendingUser.getUid(), pendingUserExpire, redisTemplate).start();//1小时后删除残留索引
            new ActivationMailThread(user.getUsername(), user.getEmail(),
                    location + "/activate.html?" + pendingUser.getUid()).start();//发生激活邮件
            log.info("注册成功 pendingUid:" + pendingUser.getUid());
        } catch (Exception e) {
            throw new SystemException(Code.REGISTER_UNKNOWN_ERR, null, "服务器繁忙,请稍后重试或尝试联系管理员");
        }
        return new Result(Code.REGISTER_OK, null, "请查收邮箱并在一小时内完成激活");
    }

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