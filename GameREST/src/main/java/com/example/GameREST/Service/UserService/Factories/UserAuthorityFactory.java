package com.example.GameREST.Service.UserService.Factories;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;

public interface UserAuthorityFactory
{
    UserAuthority create(UserEntity user,String newAuthority);
}
