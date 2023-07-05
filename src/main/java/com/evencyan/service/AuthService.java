package com.evencyan.service;

import com.evencyan.controller.Result;
import com.evencyan.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AuthService {

    Result login(User user, HttpServletResponse response);

    Result logout();

}
