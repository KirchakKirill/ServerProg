package com.example.GameREST.Service.GamePlatformService.Validator.Impl;

import com.example.GameREST.Entity.*;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Repository.GamePlatformRepository;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.GamePlatformService.Validator.GamePlatformValidator;
import org.springframework.stereotype.Service;

@Service
public class GamePlatformValidatorImpl implements GamePlatformValidator
{
    private  final GamePlatformRepository gamePlatformRepository;

    public GamePlatformValidatorImpl(GamePlatformRepository gamePlatformRepository) {
        this.gamePlatformRepository = gamePlatformRepository;
    }


    @Override
    public void validateCreate(GamePublisherEntity gamePublisher, PlatformEntity platform) {

        GamePlatformEntity gamePlatformEntity = gamePlatformRepository
                .findByGamePublisherAndPlatform(gamePublisher,platform).orElse(null);

        if  (gamePlatformEntity !=null) throw  new EntityAlreadyExistsException("Такая связь (Игра + Издатель + Платформа) уже существует:\n" +
                "или связь [Игра - Издатель] должна быть новая, или платформа, или все сразу");

    }


    @Override
    public void validateUpdate(UpdateContext context)
    {
        GamePlatformEntity gamePlatform = gamePlatformRepository.findByGamePublisherAndPlatform(
                context.getGamePublisher(),
                context.getPlatform()).orElse(null);
        if (gamePlatform != null)
        {
            throw new EntityAlreadyExistsException("Такая связь (Игра + Издатель + Платформа) уже существует");
        }

    }

}
