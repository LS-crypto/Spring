package com.playmate.service;

import com.playmate.common.BusinessException;
import com.playmate.dto.LoginRequest;
import com.playmate.dto.LoginResponse;
import com.playmate.dto.RegisterRequest;
import com.playmate.entity.User;
import com.playmate.repository.UserRepository;
import com.playmate.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException("手机号或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("手机号或密码错误");
        }

        if (user.getBanned()) {
            throw new BusinessException("账号已被封禁");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), user.getRole());
        return new LoginResponse(token, user.getRole(), sanitizeUser(user));
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("手机号已注册");
        }

        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRole("user");
        user.setBalance(java.math.BigDecimal.ZERO);

        return userRepository.save(user);
    }

    public User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Transactional
    public User updateProfile(Long userId, User updateData) {
        User user = getUserInfo(userId);
        if (updateData.getNickname() != null) user.setNickname(updateData.getNickname());
        if (updateData.getAvatar() != null) user.setAvatar(updateData.getAvatar());
        if (updateData.getGender() != null) user.setGender(updateData.getGender());
        if (updateData.getBio() != null) user.setBio(updateData.getBio());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserInfo(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // 返回用户信息时脱敏
    private Object sanitizeUser(User user) {
        user.setPassword(null);
        return user;
    }
}
