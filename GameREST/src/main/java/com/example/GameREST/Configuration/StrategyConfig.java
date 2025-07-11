package com.example.GameREST.Configuration;

import com.example.GameREST.Service.GamePlatformService.Strategies.UpdateStrategy;
import com.example.GameREST.Service.GamePlatformService.Factories.UpdateStrategyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StrategyConfig
{
    @Bean
    public List<UpdateStrategy> updateStrategies(
            UpdateStrategyFactory factory
    ) {
        return List.of(factory.createGameStrategy(),
                factory.cratePublisherStrategy(),
                factory.createPlatformStrategy(),
                factory.createGamePublisherStrategy());
    }


}
