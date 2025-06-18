package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public interface UserService {

    Optional<UserEntity> findByUserName(String username);

    UserEntity save(String username, String password);

    boolean existsByUserName(String username);
}
