package com.evencyan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.evencyan.dao.PermissionDAO;
import com.evencyan.dao.RoleDAO;
import com.evencyan.dao.UserDAO;
import com.evencyan.domain.LoginUser;
import com.evencyan.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PermissionDAO permissionDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
//                .select(User::getUid, User::getUsername, User::getPassword, User::getStatus, User::getIsEnabled);
        User user = userDAO.selectOne(queryWrapper);
        if (user == null) throw new RuntimeException("用户名或密码错误");
        user.setRoles(roleDAO.selectRolesByUid(user.getUid()));
        user.setPermissions(permissionDAO.selectByUid(user.getUid()));
        System.out.println(user);
        return new LoginUser(user);
    }
}
