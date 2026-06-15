package com.homestay.service;

import com.homestay.dto.*;
import com.homestay.entity.User;
import com.homestay.repository.UserRepository;
import com.homestay.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Result<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRole(user.getRole());
        resp.setNickname(user.getNickname());
        return Result.success(resp);
    }

    public Result<String> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return Result.error("用户名已存在");
        }
        if (!request.getRole().equals("GUEST") && !request.getRole().equals("HOST")) {
            return Result.error("角色只能为GUEST或HOST");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        userRepository.save(user);
        return Result.success("注册成功");
    }
}
