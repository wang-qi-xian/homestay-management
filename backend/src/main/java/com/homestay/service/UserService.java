package com.homestay.service;

import com.homestay.dto.Result;
import com.homestay.entity.User;
import com.homestay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Result<List<User>> listAll() {
        return Result.success(userRepository.findAll());
    }

    public Result<String> delete(Long id) {
        userRepository.deleteById(id);
        return Result.success("删除成功");
    }
}
