package com.homestay.repository;

import com.homestay.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostId(Long hostId);
    List<Room> findByAvailableTrue();
}
