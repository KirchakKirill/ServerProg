package com.example.GameREST.Service.GameService.Factories.Impl;

import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.GameService.CreationPolicy.GameCreationPolicy;
import com.example.GameREST.Service.GameService.CreationPolicy.Impl.LinkedGameCreationPolicyImpl;
import com.example.GameREST.Service.GameService.CreationPolicy.Impl.StrictGameCreationPolicyImpl;
import com.example.GameREST.Service.GameService.Factories.GameCreationPolicyFactory;

public class GameCreationPolicyFactoryImpl  implements GameCreationPolicyFactory
{
    private final GameRepository gameRepository;

    public GameCreationPolicyFactoryImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameCreationPolicy createStrictGameCreationPolicy() {
        return new StrictGameCreationPolicyImpl(gameRepository);
    }

    @Override
    public GameCreationPolicy createLinkedGameCreationPolicy() {
        return new LinkedGameCreationPolicyImpl(gameRepository);
    }
}
