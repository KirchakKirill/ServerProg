package com.example.GameREST.Configuration;

import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.GameService.CreationPolicy.GameCreationPolicy;
import com.example.GameREST.Service.GameService.Factories.GameCreationPolicyFactory;
import com.example.GameREST.Service.GameService.Factories.Impl.GameCreationPolicyFactoryImpl;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CreationPolicyConfig
{

    @Bean
    public Map<CreationPolicyState, GameCreationPolicy> gameCreationPolicyMap(GameCreationPolicyFactory factory)
    {
        return Map.of(CreationPolicyState.STRICT, factory.createStrictGameCreationPolicy(),
                CreationPolicyState.LINKED,factory.createLinkedGameCreationPolicy());
    }

    @Bean
    public GameCreationPolicyFactory gameCreationPolicyFactory(GameRepository gameRepository)
    {
        return new GameCreationPolicyFactoryImpl(gameRepository);
    }

}
