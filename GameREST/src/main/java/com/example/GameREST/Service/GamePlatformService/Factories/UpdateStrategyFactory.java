package com.example.GameREST.Service.GamePlatformService.Factories;


import com.example.GameREST.Service.GamePlatformService.Strategies.UpdateStrategy;

public interface UpdateStrategyFactory {
    UpdateStrategy createGameStrategy();
    UpdateStrategy cratePublisherStrategy();
    UpdateStrategy createPlatformStrategy();
    UpdateStrategy createGamePublisherStrategy();
}
