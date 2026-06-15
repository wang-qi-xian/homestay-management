package com.homestay.controller;

import com.homestay.dto.OrderRequest;
import com.homestay.dto.Result;
import com.homestay.entity.Order;
import com.homestay.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 游客 - 创建订单
    @PostMapping
    public Result<Order> create(@Valid @RequestBody OrderRequest request, HttpServletRequest httpRequest) {
        Long guestId = (Long) httpRequest.getAttribute("userId");
        return orderService.create(request, guestId);
    }

    // 游客 - 我的订单
    @GetMapping("/guest")
    public Result<List<Order>> listByGuest(HttpServletRequest request) {
        Long guestId = (Long) request.getAttribute("userId");
        return orderService.listByGuest(guestId);
    }

    // 房东 - 收到的订单
    @GetMapping("/host")
    public Result<List<Order>> listByHost(HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return orderService.listByHost(hostId);
    }

    // 房东 - 确认订单
    @PutMapping("/{id}/confirm")
    public Result<Order> confirm(@PathVariable Long id, HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return orderService.confirm(id, hostId);
    }

    // 房东 - 完成订单
    @PutMapping("/{id}/complete")
    public Result<Order> complete(@PathVariable Long id, HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return orderService.complete(id, hostId);
    }

    // 取消订单（游客/房东均可）
    @PutMapping("/{id}/cancel")
    public Result<Order> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        return orderService.cancel(id, userId, role);
    }

    // 管理员 - 所有订单
    @GetMapping("/admin")
    public Result<List<Order>> listAll(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) return Result.error(403, "无权访问");
        return orderService.listAll();
    }
}
