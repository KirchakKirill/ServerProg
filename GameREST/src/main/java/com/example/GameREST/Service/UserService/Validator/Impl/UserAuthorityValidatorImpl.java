package com.example.GameREST.Service.UserService.Validator.Impl;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Service.UserService.Validator.UserAuthorityValidator;
import org.springframework.stereotype.Component;

@Component
public class UserAuthorityValidatorImpl implements UserAuthorityValidator {


    @Override
    public void validate(UserEntity user,String newAuthority)
    {
        long existingAuthorities = user.getAuthorities()
                .stream()
                .map(UserAuthority::getAuthority)
                .filter((a)-> a.equals("ROLE_"+newAuthority))
                .count();

        if (existingAuthorities > 0)
        {
            throw new EntityAlreadyExistsException("Такая роль уже есть у этого пользователя");
        }
    }
}
