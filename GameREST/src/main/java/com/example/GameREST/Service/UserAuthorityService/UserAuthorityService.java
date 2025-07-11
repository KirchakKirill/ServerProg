package com.example.GameREST.Service.UserAuthorityService;

import com.example.GameREST.Entity.UserAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserAuthorityService {
    List<UserAuthority> findAuthoritiesByUser(Long id);
}
