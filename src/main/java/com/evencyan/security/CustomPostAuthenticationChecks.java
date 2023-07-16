package com.evencyan.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomPostAuthenticationChecks implements UserDetailsChecker {

    @Override
    public void check(UserDetails loginUser) {
        if (!loginUser.isCredentialsNonExpired()) {
            log.debug("由于用户帐户凭据已过期，无法进行身份验证");
            throw new CredentialsExpiredException("用户账户凭据已过期");
        }
    }

}
