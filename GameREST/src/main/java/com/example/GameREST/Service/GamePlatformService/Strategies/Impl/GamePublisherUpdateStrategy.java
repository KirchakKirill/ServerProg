package com.example.GameREST.Service.GamePlatformService.Strategies.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.GamePublisherService.GamePublisherService;
import com.example.GameREST.Service.GamePlatformService.Strategies.UpdateStrategy;

public class GamePublisherUpdateStrategy implements UpdateStrategy
{
    private final GamePublisherService gamePublisherService;

    public GamePublisherUpdateStrategy(GamePublisherService gamePublisherService) {
        this.gamePublisherService = gamePublisherService;
    }


    @Override
    public void update(UpdateContext context) {

        GamePublisherEntity currentGamePublisher = context.getGamePlatformEntity().getGamePublisher();
        GamePublisherEntity newGamePublisher;

        GameEntity game = context.getGame();
        PublisherEntity publisher = context.getPublisher();

        if (!currentGamePublisher.getGame().equals(game)
                || !currentGamePublisher.getPublisher().equals(publisher))
        {
            newGamePublisher  = gamePublisherService
                    .findByGameAndPublisher(game,publisher)
                    .orElseGet(()-> gamePublisherService.saveOnlyNewEntity(
                            GamePublisherEntity.builder()
                                    .game(game)
                                    .publisher(publisher)
                                    .build()));
            context.setGamePublisher(newGamePublisher);
        }
    }
}
