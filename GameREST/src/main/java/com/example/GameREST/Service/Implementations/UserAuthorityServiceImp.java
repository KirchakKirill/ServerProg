package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Repository.UserAuthorityRepository;
import com.example.GameREST.Repository.UserRepository;
import com.example.GameREST.Service.Interfaces.UserAuthorityService;
import com.example.GameREST.Service.Interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserAuthorityServiceImp implements UserAuthorityService
{


    private final UserAuthorityRepository authorityRepository;
    private final UserService userService;

    public UserAuthorityServiceImp(UserAuthorityRepository authorityRepository, UserService userService) {
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }



    @Override
    public UserAuthority save(String username, String role) {
        role = role.toUpperCase();
        var user1 = userService.findByUserName(username).orElse(null);
        log.info(String.valueOf(user1));
        if(user1==null) throw new NullPointerException("Такого юзера не существует");

        UserAuthority authority = UserAuthority.builder()
                .authority("ROLE_"+role)
                .user(user1)
                .build();

        return authorityRepository.saveAndFlush(authority);
    }


}
