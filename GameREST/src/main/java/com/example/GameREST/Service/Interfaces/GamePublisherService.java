package com.example.GameREST.Service.Interfaces;


import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.PublisherEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GamePublisherService {

    GamePublisherEntity createOrFind(RequestGamePlatformDTO requestGamePlatformDTO);

    Optional<GamePublisherEntity> findByGameAndPublisher(GameEntity game,
                                                                 PublisherEntity publisher);
}
