package com.example.GameREST.Service.GameService;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GameService {
    Optional<GameEntity> findById(Long id);
    GameEntity addNewGame(String gameName, GenreEntity genre, CreationPolicyState creationPolicy);
    void updateGame(Long id, String gameName, GenreEntity updateGenre,boolean forceUpdate);
    void deleteGame(Long id, boolean forceDelete);
    Page<GameEntity> findAllByGenreWithPaging(String genre, Pageable pageable);
    Optional<GameEntity> findByGameName( String name);
    Page<GameEntity> allGamesWithPaging(PageRequest pageRequest);
}
