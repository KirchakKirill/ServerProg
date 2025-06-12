package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.GamePlatformEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.PlatformEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GamePlatformSevice {

    GamePlatformEntity save(RequestGamePlatformDTO requestGamePlatformDTO);
    Optional<GamePlatformEntity> findByGamePublisherAndPlatformAndReleaseYear(GamePublisherEntity gamePublisherEntity, PlatformEntity platformEntity, Integer releaseYear);

    List<GamePlatformEntity> getAll();

    Optional<GamePlatformEntity> findById(Long id);

    void update(GamePlatformEntity gamePlatform, RequestGamePlatformDTO requestGamePlatformDTO);

    void delete(Long id);

}
