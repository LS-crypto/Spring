package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.dto.LoginRequest;
import com.playmate.dto.LoginResponse;
import com.playmate.dto.RegisterRequest;
import com.playmate.entity.User;
import com.playmate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.getUserInfo(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<User> updateProfile(Authentication authentication, @RequestBody User updateData) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.updateProfile(userId, updateData);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/password")
    public Result<Void> changePassword(Authentication authentication, @RequestBody Map<String, String> params) {
        Long userId = (Long) authentication.getPrincipal();
        userService.changePassword(userId, params.get("oldPassword"), params.get("newPassword"));
        return Result.success();
    }
}
