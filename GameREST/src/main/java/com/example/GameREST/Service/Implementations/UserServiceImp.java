package com.example.GameREST.Service.Implementations;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Repository.UserRepository;
import com.example.GameREST.Service.Interfaces.UserAuthorityService;
import com.example.GameREST.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserEntity> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }


    @Override
    public UserEntity save(String username, String password) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setAuthorities(new ArrayList<>());

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthority("ROLE_USER");
        userAuthority.setUser(userEntity);

        userEntity.getAuthorities().add(userAuthority);

         UserEntity user = userRepository.save(userEntity);
         userRepository.flush();
         return user;
    }

    @Override
    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }
}
