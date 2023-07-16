package com.evencyan.security.expression;

import com.evencyan.domain.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("security")
public class CustomSecurityExpressionRoot {

    public boolean hasPermission(String permission) {
        return hasAnyPermission(permission);
    }

    public boolean hasAnyPermission(String... permissions) {
        List<String> ownedPermissions = getLoginUser().getUser().getPermissions();
        for (String permission : permissions)
            if (ownedPermissions.contains(permission)) return true;
        return false;

    }

    public boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    public boolean hasAnyRole(String... roles) {
        List<String> ownedRoles = getLoginUser().getUser().getRoles();
        for (String role : roles)
            if (ownedRoles.contains(role)) return true;
        return false;
    }

    private LoginUser getLoginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
