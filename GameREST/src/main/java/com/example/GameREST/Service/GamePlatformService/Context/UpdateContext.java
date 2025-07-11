package com.example.GameREST.Service.GamePlatformService.Context;

import com.example.GameREST.DTO.RequestGamePlatformUpdateDTO;
import com.example.GameREST.Entity.*;
import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateContext
{
    private final GamePlatformEntity gamePlatformEntity;
    private final RequestGamePlatformUpdateDTO dto;

    private GameEntity game;
    private PublisherEntity publisher;
    private PlatformEntity platform;
    private GamePublisherEntity gamePublisher;

    public boolean needsGameUpdate() {
        return !gamePlatformEntity.getGamePublisher().getGame().getGameName()
                .equals(dto.getGameName());
    }
    public boolean needsPublisherUpdate() {
        return !gamePlatformEntity.getGamePublisher().getPublisher().getPublisherName()
                .equals(dto.getPublisherName());
    }
    public boolean needsPlatformUpdate() {
        return !gamePlatformEntity.getPlatform().getPlatformName()
                .equals(dto.getPlatformName());
    }

    public void setUpEntitiesForUpdate()
    {
        GamePublisherEntity defaultGamePublisher = gamePlatformEntity.getGamePublisher();

        this.game = defaultGamePublisher.getGame();
        this.publisher = defaultGamePublisher.getPublisher();
        this.platform = gamePlatformEntity.getPlatform();
        this.gamePublisher = defaultGamePublisher;
    }

}
