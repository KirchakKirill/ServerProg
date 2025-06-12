package com.example.Greenswamp.Services;


import com.example.Greenswamp.Entity.Authority;
import org.springframework.stereotype.Service;

@Service
public interface AuthorityService {

    public Authority findByAuthorityType(Authority.AuthorityType type);
}
