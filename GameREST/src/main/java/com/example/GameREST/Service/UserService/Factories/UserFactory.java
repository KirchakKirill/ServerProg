package com.example.GameREST.Service.UserService.Factories;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserFactory
{
    UserEntity create(UserDTO userDTO, PasswordEncoder passwordEncoder);
}
