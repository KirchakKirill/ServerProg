package com.example.GameREST.Service.Interfaces;

import com.example.GameREST.Entity.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GenreService {

    Optional<GenreEntity> findGenreByName(String genre);
    GenreEntity createOrFind(String genreName);
    void update(Long id,String genreName,boolean forceUpdate);
    Page<GenreEntity> findAllWithPaging(Pageable pageable);
    Optional<GenreEntity> findById(Long id);
    void delete(Long id,boolean forceDelete);

    GenreEntity create(String genreName);
}
