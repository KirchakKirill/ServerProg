package com.example.GameREST.Service.GamePublisherService.Impl;


import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.*;
import com.example.GameREST.Repository.GamePublisherRepository;
import com.example.GameREST.Service.GamePublisherService.GamePublisherService;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GameService.GameService;
import com.example.GameREST.Service.GenreService.GenreService;
import com.example.GameREST.Service.PublisherService.PublisherService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GamePublisherServiceImpl implements GamePublisherService {


    private final  GamePublisherRepository gamePublisherRepository;
    private final  GameService gameService;
    private final  GenreService genreService;
    private final PublisherService publisherService;
    private final CreationPolicyState POLICY_STATE = CreationPolicyState.LINKED;


    public GamePublisherServiceImpl(GamePublisherRepository gamePublisherRepository, GameService gameService, GenreService genreService, PublisherService publisherService) {
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

    @Override
    public GamePublisherEntity saveOnlyNewEntity(GamePublisherEntity gamePublisher) {
        return   gamePublisherRepository.save(gamePublisher);
    }


    private GameEntity getGame(RequestGamePlatformDTO requestGamePlatformDTO) {
            /* если жанр существует, то вернется в currentGenre, если нет, то создастся и также вернется */
        GenreEntity currentGenre = genreService.create(requestGamePlatformDTO.getGenreName(),POLICY_STATE);
        return gameService.addNewGame(requestGamePlatformDTO.getGameName(), currentGenre,POLICY_STATE);
    }

    private PublisherEntity getPublisher( String publisherName){
            /* если издатель существует, то метод save его вернет, если нет, то создаст, сохранит в БД и вернет */
            return  publisherService.create(publisherName,POLICY_STATE);
    }



}
