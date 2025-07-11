package com.example.GameREST.Service.UserService.Validator;

import com.example.GameREST.Entity.UserEntity;

public interface UserAuthorityValidator
{
    void validate(UserEntity user,String newAuthority);
}
