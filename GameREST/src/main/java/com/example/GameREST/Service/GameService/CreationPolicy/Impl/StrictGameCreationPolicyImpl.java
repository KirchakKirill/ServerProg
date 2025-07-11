package com.example.GameREST.Service.GameService.CreationPolicy.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.GameService.CreationPolicy.GameCreationPolicy;
import com.example.GameREST.Service.GameService.DataPackage.GamePackage;

import java.util.Optional;

public class StrictGameCreationPolicyImpl implements GameCreationPolicy
{
    private final GameRepository gameRepository;

    public StrictGameCreationPolicyImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameEntity apply(GamePackage gameData) {
        Optional<GameEntity> existing =  gameRepository.findByGameName(gameData.getGameName());
        existing.ifPresent((ex)-> {

            throw new EntityAlreadyExistsException("Такая игра уже существует");
        });
        return  GameEntity.builder()
                .gameName(gameData.getGameName())
                .genre(gameData.getGenre())
                .build();
    }
}
