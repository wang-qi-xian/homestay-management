package com.homestay.service;

import com.homestay.dto.OrderRequest;
import com.homestay.dto.Result;
import com.homestay.entity.Order;
import com.homestay.entity.Room;
import com.homestay.repository.OrderRepository;
import com.homestay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;

    public Result<Order> create(OrderRequest request, Long guestId) {
        Room room = roomRepository.findById(request.getRoomId()).orElse(null);
        if (room == null) return Result.error("房间不存在");
        if (!room.getAvailable()) return Result.error("房间不可预订");

        if (request.getCheckOut().isBefore(request.getCheckIn()) ||
            request.getCheckIn().isBefore(LocalDate.now())) {
            return Result.error("日期不合法");
        }

        long days = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        if (days <= 0) return Result.error("至少预订1晚");

        Order order = new Order();
        order.setGuestId(guestId);
        order.setRoomId(room.getId());
        order.setHostId(room.getHostId());
        order.setCheckIn(request.getCheckIn());
        order.setCheckOut(request.getCheckOut());
        order.setTotalPrice(room.getPrice().multiply(BigDecimal.valueOf(days)));
        order.setStatus("PENDING");
        return Result.success(orderRepository.save(order));
    }

    public Result<List<Order>> listByGuest(Long guestId) {
        return Result.success(orderRepository.findByGuestId(guestId));
    }

    public Result<List<Order>> listByHost(Long hostId) {
        return Result.success(orderRepository.findByHostId(hostId));
    }

    public Result<List<Order>> listAll() {
        return Result.success(orderRepository.findAll());
    }

    public Result<Order> confirm(Long orderId, Long hostId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return Result.error("订单不存在");
        if (!order.getHostId().equals(hostId)) return Result.error("无权操作");
        if (!order.getStatus().equals("PENDING")) return Result.error("订单状态不可操作");
        order.setStatus("CONFIRMED");
        return Result.success(orderRepository.save(order));
    }

    public Result<Order> cancel(Long orderId, Long userId, String role) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return Result.error("订单不存在");
        if (role.equals("GUEST") && !order.getGuestId().equals(userId)) return Result.error("无权操作");
        if (role.equals("HOST") && !order.getHostId().equals(userId)) return Result.error("无权操作");
        if (!order.getStatus().equals("PENDING") && !order.getStatus().equals("CONFIRMED")) {
            return Result.error("订单状态不可取消");
        }
        order.setStatus("CANCELLED");
        return Result.success(orderRepository.save(order));
    }

    public Result<Order> complete(Long orderId, Long hostId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return Result.error("订单不存在");
        if (!order.getHostId().equals(hostId)) return Result.error("无权操作");
        if (!order.getStatus().equals("CONFIRMED")) return Result.error("订单状态不可操作");
        order.setStatus("COMPLETED");
        return Result.success(orderRepository.save(order));
    }
}
