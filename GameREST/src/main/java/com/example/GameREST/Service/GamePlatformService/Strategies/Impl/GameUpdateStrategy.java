package com.example.GameREST.Service.GamePlatformService.Strategies.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.GameService.GameService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class GameUpdateStrategy extends AbstractEntityUpdateStrategy<GameEntity>
{
    private final GameService gameService;

    public GameUpdateStrategy(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    boolean needsUpdate(UpdateContext context) {
        return context.needsGameUpdate();
    }

    @Override
    Optional<GameEntity> findEntity(UpdateContext context) {
        return gameService.findByGameName(context.getDto().getGameName());
    }

    @Override
    void setEntity(UpdateContext context, GameEntity entity) {
        context.setGame(entity);
        log.info(String.valueOf(context.getGame()));
    }

}
