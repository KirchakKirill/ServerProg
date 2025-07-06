package com.example.GameREST.Repository;

import com.example.GameREST.Entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


@Repository
public interface GamePlatformRepository extends JpaRepository<GamePlatformEntity,Long> {

        @Query("select g from GamePlatformEntity g where g.gamePublisher = :gamePublisher and g.platform = :platform")
        Optional<GamePlatformEntity> findByGamePublisherAndPlatform(@Param("gamePublisher")GamePublisherEntity gamePublisherEntity,
                                                                                  @Param("platform")PlatformEntity platformEntity
                                                                                   );


        @Modifying
        @Query("UPDATE GamePlatformEntity g SET g.gamePublisher = :gamePublisher," +
                " g.platform = :platform, g.releaseYear = :releaseYear WHERE g.id = :id")
        void update(
                @Param("id") Long id,
                @Param("gamePublisher") GamePublisherEntity gamePublisher,
                @Param("platform") PlatformEntity platform,
                @Param("releaseYear") Integer releaseYear
        );





}
