package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface UserAuthorityService {

    UserAuthority save(String username,String role);
    List<UserAuthority> findAuthoritiesByUser(Long id);
}
