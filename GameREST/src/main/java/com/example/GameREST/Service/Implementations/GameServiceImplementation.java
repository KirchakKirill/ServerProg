package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Repository.GenreRepository;
import com.example.GameREST.Service.Interfaces.GameService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public  class GameServiceImplementation implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Optional<GameEntity> findById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    @Transactional
    public GameEntity addNewGame(String gameName, GenreEntity genreAdd) {
        GameEntity gameEntity =  returnIfExists(gameName);
        if(gameEntity == null )
        {
             gameEntity = GameEntity.builder()
                    .gameName(gameName)
                    .genre(genreAdd)
                    .build();

            return  gameRepository.save(gameEntity);
        }
        else if (!gameEntity.getGenre().getGenreName().equals(genreAdd.getGenreName())) {

             gameEntity.setGenre(genreAdd);
             return gameRepository.save(gameEntity);
        }
        else {
            return gameEntity;
        }

    }

    public GameEntity returnIfExists(String gameName){

        Optional<GameEntity> gameEntity =  gameRepository.findByGameName(gameName);

        GameEntity currentGame;

        if (gameEntity.isPresent())
        {
            currentGame = gameEntity.get();
            return currentGame;
        }
        else{
            return null;

        }
    }

    @Override
    @Transactional
    public void updateGame(Long id, String gameName, GenreEntity updateGenre) {
        gameRepository.updateGame(id,gameName,updateGenre);
    }

    @Override
    @Transactional
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    @Override
    public List<GameEntity> allGames() {
        return gameRepository.findAll();
    }

    @Override
    public List<GameEntity> findAllByGenre(String genre) {
        return gameRepository.findAllByGenre(genre);
    }

    @Override
    public Optional<GameEntity> findByGameName(String name) {
        return  gameRepository.findByGameName(name);
    }


}
