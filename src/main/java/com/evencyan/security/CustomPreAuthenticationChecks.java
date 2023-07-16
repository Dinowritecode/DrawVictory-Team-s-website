package com.evencyan.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomPreAuthenticationChecks implements UserDetailsChecker {
    @Override
    public void check(UserDetails loginUser) {
        if (!loginUser.isAccountNonLocked()) {
            log.debug("由于用户帐户被锁定，无法进行身份验证");
            throw new LockedException("用户账户已被锁定，请联系管理员");
        }
        if (!loginUser.isEnabled()) {
            log.debug("由于用户帐户被禁用，无法进行身份验证");
            throw new DisabledException("用户账户已被禁用，请联系管理员");
        }
        if (!loginUser.isAccountNonExpired()) {
            log.debug("由于用户帐户已过期，无法进行身份验证");
            throw new AccountExpiredException("用户账户已过期，请联系管理员");
        }
    }
}
