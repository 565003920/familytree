package com.familytree.service;

import com.familytree.config.JwtUtil;
import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.entity.User;
import com.familytree.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        return jwtUtil.generateToken(user.getUsername());
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }

        return jwtUtil.generateToken(user.getUsername());
    }
}
