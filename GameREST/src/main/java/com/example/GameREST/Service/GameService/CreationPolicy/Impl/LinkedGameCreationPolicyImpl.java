package com.example.GameREST.Service.GameService.CreationPolicy.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.GameService.CreationPolicy.GameCreationPolicy;
import com.example.GameREST.Service.GameService.DataPackage.GamePackage;

import java.util.Optional;

public class LinkedGameCreationPolicyImpl implements GameCreationPolicy
{
    private final GameRepository gameRepository;

    public LinkedGameCreationPolicyImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    @Override
    public GameEntity apply(GamePackage gameData) {
        Optional<GameEntity> existing =  gameRepository.findByGameName(gameData.getGameName());
        existing.ifPresent((ex)-> {
            if (!ex.getGenre().equals(gameData.getGenre()))
            {
                throw new EntityAlreadyExistsException(String.format("Игра с таким названием уже существует, но другом жанре,\n" +
                                "используйте подходящий жанр или придумайте уникальное название игры"));
            }
        });
        return existing.orElseGet(() -> GameEntity.builder()
                .gameName(gameData.getGameName())
                .genre(gameData.getGenre())
                .build());
    }
}
