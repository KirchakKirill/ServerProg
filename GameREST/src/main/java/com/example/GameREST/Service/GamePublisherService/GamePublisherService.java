package com.example.GameREST.Service.GamePublisherService;


import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.PublisherEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GamePublisherService {

    GamePublisherEntity createOrFind(RequestGamePlatformDTO requestGamePlatformDTO);

    Optional<GamePublisherEntity> findByGameAndPublisher(GameEntity game,
                                                                 PublisherEntity publisher);
    GamePublisherEntity saveOnlyNewEntity(GamePublisherEntity gamePublisher);
}
