package com.example.GameREST.Service.UserService.Factories.Impl;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Service.UserService.Factories.UserFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DefaultUserFactoryImpl implements UserFactory
{

    @Override
    public UserEntity create(UserDTO userDTO, PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .authorities(new ArrayList<>())
                .build();
    }
}
