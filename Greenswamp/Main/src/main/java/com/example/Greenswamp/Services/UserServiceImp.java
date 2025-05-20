package com.example.Greenswamp.Services;


import com.example.Greenswamp.Entity.User;
import com.example.Greenswamp.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements  UserService{

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUser();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }
}
