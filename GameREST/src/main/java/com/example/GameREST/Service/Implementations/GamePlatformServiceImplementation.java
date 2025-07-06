package com.example.GameREST.Service.Implementations;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.DTO.RequestGamePlatformUpdateDTO;
import com.example.GameREST.Entity.*;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Repository.GamePlatformRepository;
import com.example.GameREST.Repository.GamePublisherRepository;
import com.example.GameREST.Service.Interfaces.*;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GamePlatformServiceImplementation implements GamePlatformSevice
{


    private final  GamePlatformRepository gamePlatformRepository;


    private final GamePublisherService gamePublisherService;


    private final PlatformService platformService;

    private final GameService gameService;

    private final PublisherService publisherService;

    private final GamePublisherRepository gamePublisherRepository;

    public GamePlatformServiceImplementation(GamePlatformRepository gamePlatformRepository, GamePublisherService gamePublisherService, PlatformService platformService, GameService gameService, PublisherService publisherService, GamePublisherRepository gamePublisherRepository) {
        this.gamePlatformRepository = gamePlatformRepository;
        this.gamePublisherService = gamePublisherService;
        this.platformService = platformService;
        this.gameService = gameService;
        this.publisherService = publisherService;
        this.gamePublisherRepository = gamePublisherRepository;
    }

    @Override
    @Transactional
    public GamePlatformEntity save(RequestGamePlatformDTO requestGamePlatformDTO) {

        GamePublisherEntity gamePublisher = gamePublisherService.createOrFind(requestGamePlatformDTO);
        PlatformEntity platform = platformService.createOrFind(requestGamePlatformDTO.getPlatformName());

        GamePlatformEntity gamePlatformEntity = gamePlatformRepository
                .findByGamePublisherAndPlatform(gamePublisher,platform).orElse(null);

        if  (gamePlatformEntity !=null) throw  new  EntityAlreadyExistsException("Такая связь (Игра + Издатель + Платформа) уже существует:\n" +
                "или связь [Игра - Издатель] должна быть новая, или платформа, или все сразу",
                GamePlatformEntity.class.getSimpleName(),
                gamePlatformEntity.getId());


        gamePlatformEntity = GamePlatformEntity.builder()
                    .gamePublisher(gamePublisher)
                    .releaseYear(requestGamePlatformDTO.getReleaseYear())
                    .platform(platform)
                    .build();

        return gamePlatformRepository.save(gamePlatformEntity);

    }

    @Override
    public Page<GamePlatformEntity> getAll(Pageable pageable) {

        return gamePlatformRepository.findAll(pageable);
    }

    @Override
    public Optional<GamePlatformEntity> findById(Long id) {
        return gamePlatformRepository.findById(id);
    }

    @Override
    @Transactional
    public void update(Long id , RequestGamePlatformUpdateDTO updateDTO){

        GamePlatformEntity gamePlatformEntity = gamePlatformRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("GamePlatform не найден"));

        GamePublisherEntity currentGamePublisher = gamePlatformEntity.getGamePublisher();
        GameEntity currentGame = currentGamePublisher.getGame();
        PublisherEntity currentPublisher = currentGamePublisher.getPublisher();

        if (!currentGame.getGameName().equals(updateDTO.getGameName()))
        {

            currentGame = gameService.findByGameName(updateDTO.getGameName())
                    .orElseThrow(()-> new NoSuchElementException("Игра не найдена"));

        }

        if (!currentPublisher.getPublisherName().equals(updateDTO.getPublisherName()))
        {
            currentPublisher = publisherService.findPublisherByName(updateDTO.getPublisherName())
                    .orElseThrow(()-> new NoSuchElementException("Издатель не найден"));

        }

        GamePublisherEntity newGamePublisher = currentGamePublisher;
        if (!currentGamePublisher.getGame().equals(currentGame)
                || !currentGamePublisher.getPublisher().equals(currentPublisher))
        {
            final GameEntity finalCurrentGame = currentGame;
            final PublisherEntity finalCurrentPublisher = currentPublisher;

            newGamePublisher  = gamePublisherService
                    .findByGameAndPublisher(currentGame,currentPublisher)
                    .orElseGet(()-> gamePublisherRepository.save(
                            GamePublisherEntity.builder()
                            .game(finalCurrentGame)
                            .publisher(finalCurrentPublisher)
                            .build()));
        }

        PlatformEntity newPlatform = gamePlatformEntity.getPlatform();
        if (!gamePlatformEntity.getPlatform().getPlatformName().equals(updateDTO.getPlatformName())) {

            newPlatform   = platformService.findPlatformByName(updateDTO.getPlatformName())
                    .orElseThrow(() -> new NoSuchElementException("Платформа " +
                            "не найдена"));
        }


        GamePlatformEntity gamePlatform = gamePlatformRepository.findByGamePublisherAndPlatform(
                newGamePublisher,
                newPlatform).orElse(null);
        if (gamePlatform != null)
        {
            throw new EntityAlreadyExistsException("Такая связь (Игра + Издатель + Платформа) уже существует",
                                                    GamePlatformEntity.class.getSimpleName(),
                                                    gamePlatform.getId());
        }

        gamePlatformRepository.update(gamePlatformEntity.getId(),newGamePublisher,newPlatform, updateDTO.getReleaseYear());
    }

    @Override
    public void delete(Long id) {
        gamePlatformRepository.deleteById(id);
    }

}
