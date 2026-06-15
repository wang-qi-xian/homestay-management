package com.homestay.controller;

import com.homestay.dto.Result;
import com.homestay.entity.User;
import com.homestay.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 管理员 - 用户列表
    @GetMapping("/admin")
    public Result<List<User>> listAll(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) return Result.error(403, "无权访问");
        return userService.listAll();
    }

    // 管理员 - 删除用户
    @DeleteMapping("/admin/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) return Result.error(403, "无权访问");
        return userService.delete(id);
    }
}
