package com.example.GameREST.Service.UserAuthorityService.Impl;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Repository.UserAuthorityRepository;
import com.example.GameREST.Service.UserAuthorityService.UserAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserAuthorityServiceImp implements UserAuthorityService
{
    private final UserAuthorityRepository authorityRepository;

    public UserAuthorityServiceImp(UserAuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }
    @Override
    public List<UserAuthority> findAuthoritiesByUser(Long id) {
        return authorityRepository.findAuthoritiesByUser(id);
    }


}
