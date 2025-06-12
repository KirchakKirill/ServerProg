package com.example.Greenswamp.Services;

import com.example.Greenswamp.Entity.Authority;
import com.example.Greenswamp.Repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImp implements AuthorityService{

    @Autowired
    private AuthorityRepository authorityRepository;


    @Override
    public Authority findByAuthorityType(Authority.AuthorityType type) {
        return authorityRepository.findByAuthorityType(type);
    }
}
