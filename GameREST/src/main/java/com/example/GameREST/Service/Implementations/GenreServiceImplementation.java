package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.GenreRepository;
import com.example.GameREST.Service.Interfaces.GenreService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GenreServiceImplementation implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImplementation(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;

    }

    @Override
    public Optional<GenreEntity> findGenreByName(String genre) {
        return genreRepository.findGenreByName(genre);
    }

    @Override
    public GenreEntity createOrFind(String genreName) {
        GenreEntity genre = genreRepository.findGenreByName(genreName).orElse(null);

        if (genre == null) {
            genre = GenreEntity.builder()
                    .genreName(genreName)
                    .build();
            return genreRepository.save(genre);
        } else {
            return genre;
        }

    }

    @Override
    @Transactional
    public GenreEntity create(String genreName) {
        GenreEntity existing = genreRepository.findGenreByName(genreName).orElse(null);
        if (existing != null) {
            throw new UniqueConstraintViolationException(
                    "Вы пытаетесь добавить жанр, игнорируя ограничение уникальности названия",
                    GenreEntity.class.getSimpleName(),
                    existing.getId(),
                    genreName);
        }

        return genreRepository.save(GenreEntity.builder()
                .genreName(genreName)
                .build());
    }


    @Override
    @Transactional
    public void update(Long id, String genreName, boolean forceUpdate) {
        GenreEntity genre = genreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Жанр не найден"));

        Integer count = genreRepository.existsGameByGenre(genre);

        if (count > 0 && !forceUpdate) {
            throw new BusinessLogicException("Жанр связан с " + count + " играми." +
                    "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceUpdate = true");
        }
        GenreEntity existing = genreRepository.findGenreByName(genreName).orElse(null);
        if (existing ==null)
        {
            genreRepository.update(id, genreName);
        }
        else {
            throw new UniqueConstraintViolationException("Жанр с таким названием уже существует",
                    GenreEntity.class.getSimpleName(),
                    existing.getId(),
                    genreName);
        }

    }

    @Override
    public Page<GenreEntity> findAllWithPaging(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    @Override
    public Optional<GenreEntity> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Long id,boolean forceDelete) {
        GenreEntity genre = genreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Жанр не найден"));

        Integer count = genreRepository.existsGameByGenre(genre);

        if (count > 0 && !forceDelete) {
            throw new BusinessLogicException("Жанр связан с " + count + " играми." +
                    "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceDelete = true");
        }

        genreRepository.delete(genre);

    }
}


