package com.example.GameREST.Service.UserService.Factories.Impl;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Service.UserService.Factories.UserAuthorityFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserAuthorityFactoryImpl implements UserAuthorityFactory
{
    private final String AUTHORITY = "ROLE_USER";

    @Override
    public UserAuthority create(UserEntity user,String newAuthority) {
        return UserAuthority.builder()
                .user(user)
                .authority(newAuthority==null ? AUTHORITY : convertToAuthorityView(newAuthority))
                .build();
    }

    private String convertToAuthorityView(String newAuthority){return "ROLE_" + newAuthority;}
}
