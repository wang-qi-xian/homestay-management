package com.homestay.controller;

import com.homestay.dto.Result;
import com.homestay.entity.Room;
import com.homestay.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // 公开接口 - 游客浏览
    @GetMapping("/public")
    public Result<List<Room>> listAvailable() {
        return roomService.listAvailable();
    }

    // 房东 - 我的房间
    @GetMapping("/host")
    public Result<List<Room>> listByHost(HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return roomService.listByHost(hostId);
    }

    // 房东 - 添加房间
    @PostMapping
    public Result<Room> create(@RequestBody Room room, HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return roomService.create(room, hostId);
    }

    // 房东 - 修改房间
    @PutMapping("/{id}")
    public Result<Room> update(@PathVariable Long id, @RequestBody Room room, HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return roomService.update(id, room, hostId);
    }

    // 房东 - 删除房间
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        Long hostId = (Long) request.getAttribute("userId");
        return roomService.delete(id, hostId);
    }

    // 管理员 - 所有房间
    @GetMapping("/admin")
    public Result<List<Room>> listAll(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) return Result.error(403, "无权访问");
        return roomService.listAll();
    }

    // 管理员 - 修改房间
    @PutMapping("/admin/{id}")
    public Result<Room> adminUpdate(@PathVariable Long id, @RequestBody Room room, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) return Result.error(403, "无权访问");
        return roomService.adminUpdate(id, room);
    }

    // 管理员 - 删除房间
    @DeleteMapping("/admin/{id}")
    public Result<String> adminDelete(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) return Result.error(403, "无权访问");
        return roomService.adminDelete(id);
    }
}
