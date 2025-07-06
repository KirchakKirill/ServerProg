package com.example.GameREST.Service.Implementations;


import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.*;
import com.example.GameREST.Repository.GamePublisherRepository;
import com.example.GameREST.Service.Interfaces.GamePublisherService;
import com.example.GameREST.Service.Interfaces.GameService;
import com.example.GameREST.Service.Interfaces.GenreService;
import com.example.GameREST.Service.Interfaces.PublisherService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GamePublisherServiceImplementation implements GamePublisherService {


    private final  GamePublisherRepository gamePublisherRepository;


    private final  GameService gameService;


    private final  GenreService genreService;


    private final PublisherService publisherService;

    public GamePublisherServiceImplementation(GamePublisherRepository gamePublisherRepository, GameService gameService, GenreService genreService, PublisherService publisherService) {
        this.gamePublisherRepository = gamePublisherRepository;
        this.gameService = gameService;
        this.genreService = genreService;
        this.publisherService = publisherService;
    }


    @Override
    public GamePublisherEntity createOrFind(RequestGamePlatformDTO requestGamePlatformDTO) {

            GameEntity game = this.getGame(requestGamePlatformDTO);
            PublisherEntity publisher =  this.getPublisher(requestGamePlatformDTO.getPublisherName());

            GamePublisherEntity gamePublisher =  gamePublisherRepository
                            .findByGameAndPublisher(game,publisher).orElse(null);

            if(gamePublisher == null) {

                     gamePublisher = GamePublisherEntity.builder()
                            .game(game)
                            .publisher(publisher)
                            .build();
                    return gamePublisherRepository.save(gamePublisher);
            }
            else{
                    return gamePublisher;
            }
    }


    @Override
    public Optional<GamePublisherEntity> findByGameAndPublisher(GameEntity game, PublisherEntity publisher) {
        return  gamePublisherRepository.findByGameAndPublisher(game,publisher);
    }



    private GameEntity getGame(RequestGamePlatformDTO requestGamePlatformDTO) {
            /* если жанр существует, то вернется в currentGenre, если нет, то создастся и также вернется */
        GenreEntity currentGenre = genreService.createOrFind(requestGamePlatformDTO.getGenreName());
        return gameService.createGameForLink(requestGamePlatformDTO.getGameName(), currentGenre);
    }

    private PublisherEntity getPublisher( String publisherName){
            /* если издатель существует, то метод save его вернет, если нет, то создаст, сохранит в БД и вернет */
            return  publisherService.createOrFind(publisherName);
    }



}
