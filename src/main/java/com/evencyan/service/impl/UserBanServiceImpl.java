package com.evencyan.service.impl;

import com.evencyan.dao.UserDAO;
import com.evencyan.service.MailService;
import com.evencyan.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserBanServiceImpl {

    private final UserDAO userDAO;
    private final RedisCache redisCache;
    private final MailService mailService;

    // 每天凌晨1点执行一次
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkUserBan() {
        Map<Integer, Long> bannedUsers = redisCache.getCacheMap("banned_users");
        for (Map.Entry<Integer, Long> bannedUser : bannedUsers.entrySet()) {
            if (bannedUser.getValue() < System.currentTimeMillis()) {
                redisCache.delCacheMapValue("banned_users", bannedUser.getKey().toString()); // 解封用户
                userDAO.unban(bannedUser.getKey()); // 更新用户状态
            }
        }
    }

    public void banUser(Integer uid, long days) {
        long unbanTimestamp = System.currentTimeMillis() + days * 24 * 60 * 60 * 1000;
        redisCache.setCacheMapValue("banned_users", uid.toString(), String.valueOf(unbanTimestamp));
        // TODO 发送邮件通知用户
        userDAO.ban(uid); // 更新用户状态
    }

    public void unbanUser(Integer uid) {
        redisCache.delCacheMapValue("banned_users", uid.toString());
        userDAO.unban(uid); // 更新用户状态
    }

}
