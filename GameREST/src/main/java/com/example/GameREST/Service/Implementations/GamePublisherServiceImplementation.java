package com.example.GameREST.Service.Implementations;


import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.Entity.*;
import com.example.GameREST.Repository.GamePublisherRepository;
import com.example.GameREST.Service.Interfaces.GamePublisherService;
import com.example.GameREST.Service.Interfaces.GameService;
import com.example.GameREST.Service.Interfaces.GenreService;
import com.example.GameREST.Service.Interfaces.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class GamePublisherServiceImplementation implements GamePublisherService {

    @Autowired
    private GamePublisherRepository gamePublisherRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PublisherService publisherService;


    @Override
    @Transactional
    public GamePublisherEntity save(RequestGamePlatformDTO requestGamePlatformDTO) {

            GameEntity game = this.getGame(requestGamePlatformDTO);
            PublisherEntity publisher =  this.getPublisher(requestGamePlatformDTO.getPublisherName());

            GamePublisherEntity gamePublisher = returnIfExists(game,publisher);

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
    @Transactional
    public void update(Long id, GameEntity game, PublisherEntity publisher, RequestGamePlatformDTO requestGamePlatformDTO) throws IllegalArgumentException,ResponseStatusException {

        GameEntity findGame = gameService.findByGameName(requestGamePlatformDTO.getGameName())
                .orElseThrow(()->  new IllegalArgumentException("Некорректная  игра " +
                        "или такой не существует"));


        PublisherEntity findPublisher = publisherService.findPublisherByName(requestGamePlatformDTO.getPublisherName())
                        .orElseThrow(() -> new IllegalArgumentException("Некорректный  издатель " +
                                "или такого не существует"));

        GenreEntity genre = genreService.findGenreByName(requestGamePlatformDTO.getGenreName())
                .orElseThrow(()-> new IllegalArgumentException("Некорректный  жанр " +
                        "или такой не существует"));


        GamePublisherEntity gamePublisher = gamePublisherRepository.findById(id).get();


            if(gamePublisher.getGame().getId().equals(findGame.getId())
                    && gamePublisher.getPublisher().getId().equals(findPublisher.getId())) {

                if (findGame.getGenre().getGenreName().equals(requestGamePlatformDTO.getGenreName())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Данные не изменены:  сущность уже содержит указанные значения [gameName,publisherName,genreName]");
                }
                else {
                    gameService.updateGame(findGame.getId(), findGame.getGameName(), genre);
                }
            }
            else
            {
                gamePublisherRepository.update(id,findGame,findPublisher);
            }

    }



    public GamePublisherEntity returnIfExists(GameEntity gameEntity, PublisherEntity publisherEntity){
                Optional<GamePublisherEntity> gamePublisher =  gamePublisherRepository
                        .findByGameAndPublisher(gameEntity,publisherEntity);
                GamePublisherEntity currentGamePublisher;

                if (gamePublisher.isPresent())
                {
                        currentGamePublisher = gamePublisher.get();
                        return currentGamePublisher;
                }
                else{
                        return null;

                }
        }


    public GameEntity getGame(RequestGamePlatformDTO requestGamePlatformDTO) {
            /* если жанр существует, то вернется в currentGenre, если нет, то создастся и также вернется */
            GenreEntity currentGenre = genreService.save(requestGamePlatformDTO.getGenreName());
            return gameService.addNewGame(requestGamePlatformDTO.getGameName(), currentGenre);
    }


    public PublisherEntity getPublisher( String publisherName){
            /* если издатель существует, то метод save его вернет, если нет, то создаст, сохранит в БД и вернет */
            return  publisherService.save(publisherName);
    }



}
