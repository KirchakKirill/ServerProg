package com.example.GameREST.Repository;

import com.example.GameREST.Entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity,Long> {


    @Query("select g from GenreEntity g where g.genreName = :genre")
    Optional<GenreEntity> findGenreByName(@Param("genre") String genre);

    @Modifying
    @Query("Update GenreEntity g set g.genreName = :genreName where g.id = :id")
    public void update(@Param("id") Long id, @Param("genreName") String genreName);
}
