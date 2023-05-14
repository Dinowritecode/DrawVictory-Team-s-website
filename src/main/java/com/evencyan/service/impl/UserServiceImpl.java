package com.evencyan.service.impl;

import com.evencyan.controller.Code;
import com.evencyan.controller.Result;
import com.evencyan.dao.UserDao;
import com.evencyan.domain.User;
import com.evencyan.exception.BusinessException;
import com.evencyan.service.UserService;
import com.evencyan.thread.ActivationMailThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private Random random;
    @Value("${server.domain}")
    private String location;

    @Override
    public Result getResultById(int id) {
        User user = userDao.getById(id);
        if (user == null) return new Result(Code.GET_ID_NOT_EXIST_ERR, null, "用户不存在");
        return new Result(Code.GET_OK, user);
    }

    @Override
    public User getUserById(int id) {
        User user = userDao.getById(id);
        if (user == null) throw new BusinessException(Code.GET_ID_NOT_EXIST_ERR, null, "用户不存在");
        return user;
    }

    @Override
    public Result getResultByUsername(String username) {
        User user = userDao.getByUsername(username);
        if (user == null) throw new BusinessException(Code.GET_USERNAME_NOT_EXIST_ERR, null, "用户不存在");
        return new Result(Code.GET_OK, user);
    }

    @Override
    public Result activateUser(String token) {
        User user = userDao.getByToken(token);
        if (user == null)
            throw new BusinessException(Code.ACTIVATE_ERR, null, "抱歉，您的激活链接无效");
        if (user.getRegTime() / 1000 + 3600 > System.currentTimeMillis()) {
            userDao.delete(user);
            throw new BusinessException(Code.ACTIVATE_ERR, user, "您的激活有效期已过");
        }
        try {
            userDao.activate(user);
        } catch (Exception e) {
            throw new BusinessException(Code.ACTIVATE_ERR, user, "激活失败，未知原因");
        }
        return new Result(Code.ACTIVATE_OK, user, "账户激活成功！");
    }

    @Override
    public Result register(User user) {
        userDao.deleteInvalid();
        if (StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getPassword()) ||
                StringUtils.isEmpty(user.getEmail())
        ) throw new BusinessException(Code.REGISTER_EMPTY_DATA_ERR, user, "请正确填写信息");
        user.setUsername(user.getUsername().trim());
        user.setPassword(user.getPassword().trim());
        user.setEmail(user.getEmail().trim());
        //->用户名只允许包含数字,大小写字母,下划线和连词线,且长度在4-16位之间
        if (!user.getUsername().matches("[\\w-]{4,16}"))
            throw new BusinessException(Code.REGISTER_USERNAME_FORMAT_ERR, user, "用户名格式有误");
        //->密码需包含字母,符号或者数字中至少两项,且长度在6-16位之间
        if (!user.getPassword().matches("(?!\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}"))
            throw new BusinessException(Code.REGISTER_PASSWORD_FORMAT_ERR, user, "密码格式有误");
        if (!user.getEmail().matches("[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}"))
            throw new BusinessException(Code.REGISTER_EMAIL_FORMAT_ERR, user, "邮箱格式有误");
        if (userDao.getByUsername(user.getUsername()) != null)
            throw new BusinessException(Code.REGISTER_EXIST_USERNAME_ERR, user, "用户名已存在");
        if (userDao.getByEmail(user.getEmail()) != null)
            throw new BusinessException(Code.REGISTER_EXIST_EMAIL_ERR, user, "您的邮箱已注册过");
        String token = DigestUtils.md5Hex(user.getUsername() + random.nextInt(100000));
        user.setToken(token);
        try {
            userDao.register(user);
        } catch (Exception e) {
            throw new BusinessException(Code.REGISTER_UNKNOWN_ERR, null, "服务器繁忙,请稍后重试或尝试联系管理员");
        }
        ActivationMailThread mailSendThread = new ActivationMailThread(user.getUsername(), user.getEmail(),
                location + "/active.html?" + token);
        mailSendThread.start();//邮件发送线程
        log.info("注册成功 uid:" + user.getId());
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
}