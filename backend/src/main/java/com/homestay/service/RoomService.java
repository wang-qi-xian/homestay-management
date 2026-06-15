package com.homestay.service;

import com.homestay.dto.Result;
import com.homestay.entity.Room;
import com.homestay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Result<List<Room>> listAvailable() {
        return Result.success(roomRepository.findByAvailableTrue());
    }

    public Result<List<Room>> listByHost(Long hostId) {
        return Result.success(roomRepository.findByHostId(hostId));
    }

    public Result<List<Room>> listAll() {
        return Result.success(roomRepository.findAll());
    }

    public Result<Room> create(Room room, Long hostId) {
        room.setHostId(hostId);
        room.setAvailable(true);
        return Result.success(roomRepository.save(room));
    }

    public Result<Room> update(Long id, Room roomData, Long hostId) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) return Result.error("房间不存在");
        if (!room.getHostId().equals(hostId)) return Result.error("无权操作");
        if (roomData.getTitle() != null) room.setTitle(roomData.getTitle());
        if (roomData.getDescription() != null) room.setDescription(roomData.getDescription());
        if (roomData.getAddress() != null) room.setAddress(roomData.getAddress());
        if (roomData.getPrice() != null) room.setPrice(roomData.getPrice());
        if (roomData.getType() != null) room.setType(roomData.getType());
        if (roomData.getCapacity() != null) room.setCapacity(roomData.getCapacity());
        if (roomData.getImageUrl() != null) room.setImageUrl(roomData.getImageUrl());
        if (roomData.getAvailable() != null) room.setAvailable(roomData.getAvailable());
        return Result.success(roomRepository.save(room));
    }

    public Result<String> delete(Long id, Long hostId) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) return Result.error("房间不存在");
        if (!room.getHostId().equals(hostId)) return Result.error("无权操作");
        roomRepository.delete(room);
        return Result.success("删除成功");
    }

    public Result<Room> adminUpdate(Long id, Room roomData) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) return Result.error("房间不存在");
        if (roomData.getTitle() != null) room.setTitle(roomData.getTitle());
        if (roomData.getDescription() != null) room.setDescription(roomData.getDescription());
        if (roomData.getAddress() != null) room.setAddress(roomData.getAddress());
        if (roomData.getPrice() != null) room.setPrice(roomData.getPrice());
        if (roomData.getType() != null) room.setType(roomData.getType());
        if (roomData.getCapacity() != null) room.setCapacity(roomData.getCapacity());
        if (roomData.getImageUrl() != null) room.setImageUrl(roomData.getImageUrl());
        if (roomData.getAvailable() != null) room.setAvailable(roomData.getAvailable());
        return Result.success(roomRepository.save(room));
    }

    public Result<String> adminDelete(Long id) {
        roomRepository.deleteById(id);
        return Result.success("删除成功");
    }
}
