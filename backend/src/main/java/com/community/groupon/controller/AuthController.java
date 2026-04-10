package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.dto.LoginRequest;
import com.community.groupon.dto.LoginResponse;
import com.community.groupon.entity.User;
import com.community.groupon.service.UserService;
import com.community.groupon.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        boolean passwordMatch = false;
        try {
            passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        } catch (Exception e) {
            passwordMatch = false;
        }
        
        if (!passwordMatch) {
            passwordMatch = request.getPassword().equals(user.getPassword());
        }
        
        if (!passwordMatch) {
            return Result.error("密码错误");
        }
        
        LoginResponse response = new LoginResponse();
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setRole(user.getRole());
        
        return Result.success(response);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
