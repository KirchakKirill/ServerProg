package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Repository.GenreRepository;
import com.example.GameREST.Service.Interfaces.GenreService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreSeviceImplementation implements GenreService
{
    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Optional<GenreEntity> findGenreByName(String genre) {
        return genreRepository.findGenreByName(genre);
    }

    @Override
    @Transactional
    public GenreEntity save(String genreName) {
        var genreExistsCheck = returnIfExists(genreName);
        if (genreExistsCheck == null) {
            return genreRepository.save(GenreEntity.builder()
                    .genreName(genreName)
                    .build()
            );
        }
        else {
            return genreExistsCheck;
        }

    }

    @Override
    public void update(Long id,String genreName) {
        genreRepository.update(id,genreName);
    }

    public GenreEntity returnIfExists(String genreName){
        Optional<GenreEntity> genreEntity =  genreRepository.findGenreByName(genreName);
        GenreEntity currentGenre;

        if (genreEntity.isPresent())
        {
            currentGenre = genreEntity.get();
            return currentGenre;
        }
        else{
            return null;

        }
    }
}
