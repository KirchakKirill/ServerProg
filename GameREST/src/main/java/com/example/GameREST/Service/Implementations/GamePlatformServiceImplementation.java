package com.example.GameREST.Service.Implementations;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.*;
import com.example.GameREST.Repository.GamePlatformRepository;
import com.example.GameREST.Service.Interfaces.GamePlatformSevice;
import com.example.GameREST.Service.Interfaces.GamePublisherService;
import com.example.GameREST.Service.Interfaces.PlatformService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GamePlatformServiceImplementation implements GamePlatformSevice
{

    @Autowired
    private GamePlatformRepository gamePlatformRepository;

    @Autowired
    private GamePublisherService gamePublisherService;

    @Autowired
    private PlatformService platformService;

    @Override
    @Transactional
    public GamePlatformEntity save(RequestGamePlatformDTO requestGamePlatformDTO) {

        GamePublisherEntity gamePublisher = gamePublisherService.save(requestGamePlatformDTO);
        PlatformEntity platform = platformService.save(requestGamePlatformDTO.getPlatformName());

        GamePlatformEntity gamePlatformEntity = returnIfExists(gamePublisher,platform,requestGamePlatformDTO.getReleaseYear());
        if(gamePlatformEntity == null) {

            GamePlatformEntity newGamePlatformEntity = GamePlatformEntity.builder()
                    .gamePublisher(gamePublisher)
                    .releaseYear(requestGamePlatformDTO.getReleaseYear())
                    .platform(platform)
                    .build();
            return gamePlatformRepository.save(newGamePlatformEntity);
        }
        else {
            return gamePlatformEntity;
        }
    }

    @Override
    public Optional<GamePlatformEntity> findByGamePublisherAndPlatformAndReleaseYear(GamePublisherEntity gamePublisherEntity, PlatformEntity platformEntity,Integer releaseYear) {
        return  gamePlatformRepository.findByGamePublisherAndPlatformAndReleaseYear(gamePublisherEntity,platformEntity,releaseYear);
    }

    @Override
    public List<GamePlatformEntity> getAll() {
        return gamePlatformRepository.findAll();
    }

    @Override
    public Optional<GamePlatformEntity> findById(Long id) {
        return gamePlatformRepository.findById(id);
    }

    @Override
    @Transactional
    public void update(GamePlatformEntity gamePlatformEntity , RequestGamePlatformDTO newDataForGamePlatformUpdate) throws IllegalArgumentException, ResponseStatusException {

        gamePublisherService.update(
                gamePlatformEntity.getGamePublisher().getId(),
                gamePlatformEntity.getGamePublisher().getGame(),
                gamePlatformEntity.getGamePublisher().getPublisher(),
                newDataForGamePlatformUpdate);

        PlatformEntity findPlatform = platformService.findPlatformByName(newDataForGamePlatformUpdate.getPlatformName())
                .orElseThrow(()->  new IllegalArgumentException("Некорректная  платформа " +
                        "или такой не существует"));

        if(findPlatform.getId().equals(gamePlatformEntity.getPlatform().getId())
                && gamePlatformEntity.getReleaseYear() == newDataForGamePlatformUpdate.getReleaseYear()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,
                    "Данные не изменены: сущность уже содержит указанные значения [platformName, releaseYear]");
        }

        gamePlatformRepository.update(gamePlatformEntity.getId(),gamePlatformEntity.getGamePublisher(),findPlatform, newDataForGamePlatformUpdate.getReleaseYear());
    }

    @Override
    public void delete(Long id) {
        gamePlatformRepository.deleteById(id);
    }

    public GamePlatformEntity returnIfExists(GamePublisherEntity gamePublisherEntity, PlatformEntity platformEntity,Integer releaseYear){
        Optional<GamePlatformEntity> gamePlatformEntity =  gamePlatformRepository
                .findByGamePublisherAndPlatformAndReleaseYear(gamePublisherEntity,platformEntity,releaseYear);
        GamePlatformEntity  currentGamePlatformEntity;

        if (gamePlatformEntity.isPresent())
        {
            currentGamePlatformEntity = gamePlatformEntity.get();
            return  currentGamePlatformEntity;
        }
        else{
            return null;

        }
    }
}
