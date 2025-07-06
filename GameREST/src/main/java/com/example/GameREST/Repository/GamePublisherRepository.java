package com.example.GameREST.Repository;


import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamePublisherRepository extends JpaRepository<GamePublisherEntity,Long> {

    @Query("select g from GamePublisherEntity g where g.game = :game and g.publisher = :publisher")
    Optional<GamePublisherEntity> findByGameAndPublisher(@Param("game") GameEntity game,
                                                                @Param("publisher") PublisherEntity publisher);

    @Modifying
    @Query("Update GamePublisherEntity g set g.game = :game, g.publisher = :publisher where g.id = :id")
    void update(@Param("id") Long id,
                       @Param("game") GameEntity game,
                       @Param("publisher") PublisherEntity publisher);




}
