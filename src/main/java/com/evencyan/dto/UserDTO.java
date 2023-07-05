package com.evencyan.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private int uid;
    private String username,email;
    private List<RoleDTO> roles;
    private List<PermissionDTO> permissions;
}
