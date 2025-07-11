package com.example.GameREST.Service.UserService;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<UserEntity> findByUserName(String username);
    UserEntity save(UserDTO userDTO);
    boolean existsByUserName(String username);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByIdWithAuthorities( Long id);
    void changeAuthority(String newAuthority,Long id);

    Page<UserEntity> findAllUsers(Pageable pageable);
    UserEntity findCurrentUser();
}
