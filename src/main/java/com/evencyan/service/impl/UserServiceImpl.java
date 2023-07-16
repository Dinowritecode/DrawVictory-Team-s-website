package com.evencyan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evencyan.controller.Code;
import com.evencyan.controller.Result;
import com.evencyan.dao.PendingUserDAO;
import com.evencyan.dao.RoleDAO;
import com.evencyan.dao.UserDAO;
import com.evencyan.domain.PendingUser;
import com.evencyan.domain.User;
import com.evencyan.exception.BusinessException;
import com.evencyan.exception.SystemException;
import com.evencyan.service.MailService;
import com.evencyan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PendingUserDAO pendingUserDAO;
    private final RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("genericTaskScheduler")
    private final ThreadPoolTaskScheduler scheduler;
    @Value("${server.domain}")
    private String location;
    @Value("${config.pending-user-expire}")
    private int pendingUserExpire;// 秒

    @Override
    public Result activate(String token) {
        Optional<PendingUser> optionalUser = pendingUserDAO.findById(token);
        if (optionalUser.isEmpty())//redis中无以此为id的待激活用户
            throw new BusinessException(Code.ACTIVATE_ERR, null, "抱歉，您的激活链接无效或激活有效期已过");
        if (userDAO.insert(optionalUser.get().toUser()) == 0)
            throw new BusinessException(Code.ACTIVATE_ERR, null, "激活失败，未知原因");
        pendingUserDAO.deleteById(token);
        log.info("激活成功 username:" + optionalUser.get().getUsername());
        return Result.success(Code.ACTIVATE_OK, null, "账户激活成功！");
    }

    @Override
    public Result register(User user) {
        if (StringUtils.isBlank(user.getUsername()) ||
                StringUtils.isBlank(user.getPassword()) ||
                StringUtils.isBlank(user.getEmail())
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
        if (pendingUserDAO.findOneByUsername(user.getUsername()) != null ||
                pendingUserDAO.findOneByEmail(user.getEmail()) != null)
            throw new BusinessException(Code.REGISTER_PENDING_ACTIVATION_ERR, user, "您的邮箱或用户名已注册过，请前往邮箱激活或一小时后重试注册");
        if (userDAO.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername())) != null)
            throw new BusinessException(Code.REGISTER_EXIST_USERNAME_ERR, user, "用户名已存在");
        if (userDAO.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail())) != null)
            throw new BusinessException(Code.REGISTER_EXIST_EMAIL_ERR, user, "您的邮箱已注册过");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        PendingUser pendingUser = user.toPendingUser();
        pendingUser = pendingUserDAO.save(pendingUser);
        Integer pendingUserUid = pendingUser.getUid();
        //删除过期的pendingUser的索引
        redisTemplate.expire("pending:username:" + pendingUser.getUsername(), pendingUserExpire, TimeUnit.SECONDS);
        redisTemplate.expire("pending:email:" + pendingUser.getEmail(), pendingUserExpire, TimeUnit.SECONDS);
        redisTemplate.expire("pending:%s:idx".formatted(pendingUserUid), pendingUserExpire, TimeUnit.SECONDS);
        scheduler.schedule(() -> redisTemplate.opsForSet().remove("pending", pendingUserUid.toString()),
                Instant.now().plus(Duration.ofSeconds(pendingUserExpire)));//一小时后删除残余索引
        mailService.sendActivationMail("账号激活", pendingUser.getEmail(), pendingUser.getUsername(),
                location + "/activate.html?" + pendingUser.getUid());//发送激活邮件
        log.info("注册成功 pendingUid:" + pendingUserUid);

        return Result.success(Code.REGISTER_OK, null, "请查收邮箱并在一小时内完成激活");
    }

    @Override
    public Result setAvatar(User user) {
        User updateUser = userDAO.selectById(user.getUid());
        if (!user.getStatus()) throw new BusinessException(Code.UPDATE_ERR, user, "账户封禁中，修改头像失败");
        updateUser.setAvatar(user.getAvatar());
        if (userDAO.updateById(updateUser) == 0) throw new SystemException(Code.UPDATE_ERR, user, "头像修改失败");
        return Result.success(Code.UPDATE_OK, null, "头像修改成功");
    }

    @Override
    public Result getUser(Integer uid, List<String> fields) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select(fields).eq("uid", uid);
        User user;
        if (fields == null) return Result.success(Code.GET_OK, userDAO.selectById(uid), "获取成功");
        try {
            user = userDAO.selectOne(wrapper);
        } catch (BadSqlGrammarException e) {
            throw new BusinessException(Code.GET_ERR, null, "非法字段名");
        }
        return Result.success(Code.GET_OK, user, "获取成功");
    }
}