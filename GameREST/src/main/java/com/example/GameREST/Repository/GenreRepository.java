package com.example.GameREST.Repository;

import com.example.GameREST.Entity.GenreEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier("genreRepository")
public interface GenreRepository extends GeneralRepository<GenreEntity,Long> {

    @Modifying
    @Query("Update GenreEntity g set g.genreName = :genreName where g.id = :id")
    void update(@Param("id") Long id, @Param("genreName") String genreName);

    @Override
    @Query("select count(g) from GameEntity g where g.genre = :genre")
    Integer countLinksForEntity(@Param("genre") GenreEntity genre);

    @Override
    @Query("select g from GenreEntity g where g.genreName = :genre")
    Optional<GenreEntity> findEntityByName(@Param("genre") String genre);

    @Override
    @Query("select g.id from GenreEntity g where g.genreName = :genre")
    Optional<Long> findIdByName(@Param("genre") String genre);
}
