package com.example.GameREST.Service.GamePlatformService.Factories.Impl;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Service.GamePlatformService.Validator.Impl.GamePlatformValidatorImpl;
import com.example.GameREST.Service.GamePlatformService.Factories.GamePlatformFactory;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GamePublisherService.GamePublisherService;
import com.example.GameREST.Service.PlatformService.PlatformService;
import org.springframework.stereotype.Service;

@Service
public class GamePlatformFactoryImpl implements GamePlatformFactory
{
    private final GamePublisherService gamePublisherService;
    private final PlatformService platformService;
    private final GamePlatformValidatorImpl gamePlatformValidatorImpl;
    private final CreationPolicyState CREATION_POLICY = CreationPolicyState.LINKED;

    public GamePlatformFactoryImpl(GamePublisherService gamePublisherService, PlatformService platformService, GamePlatformValidatorImpl gamePlatformValidatorImpl) {
        this.gamePublisherService = gamePublisherService;
        this.platformService = platformService;

        this.gamePlatformValidatorImpl = gamePlatformValidatorImpl;
    }

    @Override
    public GamePlatformEntity createFromDto(RequestGamePlatformDTO requestGamePlatformDTO) {

        GamePublisherEntity gamePublisher = gamePublisherService.createOrFind(requestGamePlatformDTO);
        PlatformEntity platform = platformService.create(requestGamePlatformDTO.getPlatformName(),CREATION_POLICY);

        gamePlatformValidatorImpl.validateCreate(gamePublisher,platform);

        return  GamePlatformEntity.builder()
                .gamePublisher(gamePublisher)
                .releaseYear(requestGamePlatformDTO.getReleaseYear())
                .platform(platform)
                .build();
    }
}
