package com.example.Greenswamp.Services;

import com.example.Greenswamp.Data.UserData;
import com.example.Greenswamp.Data.UserPrincipal;
import com.example.Greenswamp.Entity.Authority;
import com.example.Greenswamp.Entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    public List<User> getAllUsers();

    public Optional<User> findUserById(Long userId);

    public void saveUser(UserData user);

    public  Optional<User> findByUsername(String username);

    public UserPrincipal loadUserByUsername(String username);

}
