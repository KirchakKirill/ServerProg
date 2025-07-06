package com.example.GameREST.Repository;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity,Long> {

    @Query("select g from GameEntity g where g.gameName = :name")
    Optional<GameEntity> findByGameName(@Param("name") String name);

    @Modifying
    @Query("UPDATE GameEntity g SET g.gameName = :newGame, g.genre = :genre WHERE g.id = :id")
    void updateGame(
            @Param("id") Long id,
            @Param("newGame") String newGame,
            @Param("genre") GenreEntity newGenre
    );

    Page<GameEntity> findByGenre(GenreEntity genre,Pageable pageable);

    @Query("SELECT COUNT(g) FROM GameEntity g WHERE g.genre = :genre")
    Integer existsWithGenre(@Param("genre") GenreEntity genre);

    @Query("SELECT COUNT(g) FROM GamePublisherEntity g WHERE g.game = :game")
    Integer existsWithGame(@Param("game") GameEntity game);
}
