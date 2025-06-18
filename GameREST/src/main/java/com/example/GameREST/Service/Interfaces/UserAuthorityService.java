package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.Entity.UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UserAuthorityService {

    UserAuthority save(String username,String role);
}
