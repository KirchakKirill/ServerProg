package com.example.Greenswamp.Services;

import com.example.Greenswamp.Entity.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public List<User> getAllUsers();

    public Optional<User> findUserById(Long userId);

}
