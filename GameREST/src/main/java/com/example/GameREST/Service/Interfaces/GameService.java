package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GameService {
    Optional<GameEntity> findById(Long id);
    GameEntity addNewGame(String gameName, GenreEntity genre);
    void updateGame(Long id, String gameName, GenreEntity updateGenre);
    void deleteGame(Long id);
    List<GameEntity> allGames();
    List<GameEntity> findAllByGenre(String genre);
    Optional<GameEntity> findByGameName( String name);

}
