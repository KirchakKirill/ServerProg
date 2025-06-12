package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.Entity.GenreEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GenreService {

    Optional<GenreEntity> findGenreByName(String genre);
    GenreEntity save(String genreName);


    public void update(Long id,String genreName);
}
