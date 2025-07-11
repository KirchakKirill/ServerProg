package com.example.GameREST.Service.GamePlatformService.Factories.Impl;

import com.example.GameREST.Service.GamePlatformService.Factories.UpdateStrategyFactory;
import com.example.GameREST.Service.GamePlatformService.Strategies.Impl.GamePublisherUpdateStrategy;
import com.example.GameREST.Service.GamePlatformService.Strategies.Impl.GameUpdateStrategy;
import com.example.GameREST.Service.GamePlatformService.Strategies.Impl.PlatformUpdateStrategy;
import com.example.GameREST.Service.GamePlatformService.Strategies.Impl.PublisherUpdateStrategy;
import com.example.GameREST.Service.GamePlatformService.Strategies.UpdateStrategy;
import com.example.GameREST.Service.GamePublisherService.GamePublisherService;
import com.example.GameREST.Service.GameService.GameService;
import com.example.GameREST.Service.PlatformService.PlatformService;
import com.example.GameREST.Service.PublisherService.PublisherService;
import org.springframework.stereotype.Component;

@Component
public class DefaultStrategyFactory implements UpdateStrategyFactory
{
    private final GameService gameService;
    private final PublisherService publisherService;
    private final PlatformService platformService;
    private final GamePublisherService gamePublisherService;

    public DefaultStrategyFactory(GameService gameService, PublisherService publisherService, PlatformService platformService, GamePublisherService gamePublisherService) {
        this.gameService = gameService;
        this.publisherService = publisherService;
        this.platformService = platformService;
        this.gamePublisherService = gamePublisherService;
    }

    @Override
    public UpdateStrategy createGameStrategy() {
        return new GameUpdateStrategy(gameService);
    }

    @Override
    public UpdateStrategy cratePublisherStrategy() {
        return new PublisherUpdateStrategy(publisherService);
    }

    @Override
    public UpdateStrategy createPlatformStrategy() {
        return new PlatformUpdateStrategy(platformService);
    }

    @Override
    public UpdateStrategy createGamePublisherStrategy() {
        return new GamePublisherUpdateStrategy(gamePublisherService);
    }
}
