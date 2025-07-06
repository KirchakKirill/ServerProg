package com.example.GameREST.Service.Implementations;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.Interfaces.GameService;
import com.example.GameREST.Service.Interfaces.GenreService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public  class GameServiceImplementation implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreService genreService;

    @Override
    public Optional<GameEntity> findById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    @Transactional
    public GameEntity addNewGame(String gameName, GenreEntity genreAdd) {
        GameEntity gameEntity = gameRepository
                .findByGameName(gameName).orElse(null);

        if(gameEntity == null )
        {
             gameEntity = GameEntity.builder()
                    .gameName(gameName)
                    .genre(genreAdd)
                    .build();

            return  gameRepository.save(gameEntity);
        }
        else {
           throw new EntityAlreadyExistsException("Такая игра уже существует",
                   GameEntity.class.getSimpleName(),
                   gameEntity.getId());
        }

    }
    @Override
    public GameEntity createGameForLink(String gameName, GenreEntity genreAdd)
    {
        GameEntity gameEntity = gameRepository
                .findByGameName(gameName).orElse(null);

        if(gameEntity == null )
        {
            gameEntity = GameEntity.builder()
                    .gameName(gameName)
                    .genre(genreAdd)
                    .build();

            return  gameRepository.save(gameEntity);
        }
        else if (!Objects.equals(gameEntity.getGenre(), genreAdd)) {
            throw new EntityAlreadyExistsException(String.format("Игра с таким названием уже существует, но в жанре %s,\n" +
                            "используйте подходящий жанр или придумайте уникальное название игры"
                    ,gameEntity.getGenre().getGenreName()),
                    GameEntity.class.getSimpleName(),
                    gameEntity.getId());
        }
        else {
            return gameEntity;
        }
    }

    @Override
    @Transactional
    public void updateGame(Long id, String gameName, GenreEntity updateGenre,boolean forceUpdate)
    {

       GameEntity findGame = gameRepository.findById(id)
               .orElseThrow(()-> new NoSuchElementException("Игра не найдена"));

       Integer count = gameRepository.existsWithGame(findGame);

        if (count>0 && !forceUpdate)
        {
            throw new BusinessLogicException("Игра имеет  " + count + " связей тип [Игра - Издатель]." +
                    "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceUpdate = true");
        }

        GameEntity existing = gameRepository.findByGameName(gameName).orElse(null);
        if (existing==null)
        {
            gameRepository.updateGame(id,gameName,updateGenre);
        }
        else {
            throw new UniqueConstraintViolationException("Игра с таким названием уже существует",
                    GameEntity.class.getSimpleName(),
                    existing.getId(),
                    gameName);
        }


    }


    @Override
    @Transactional
    public void deleteGame(Long id, boolean forceDelete) {
        GameEntity findGame= gameRepository.findById(id)
                        .orElseThrow(()->new NoSuchElementException("Игра не найдена"));

        Integer count  = gameRepository.existsWithGame(findGame);
        if (count>0 && !forceDelete)
        {
            throw new BusinessLogicException("Игра имеет  " + count + " связей тип [Игра - Издатель]." +
                    "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения." +
                    "Если уверенны, то forceDelete = true");
        }
        gameRepository.delete(findGame);
    }

    @Override
    public Page<GameEntity> findAllByGenreWithPaging(String genre, Pageable pageable) {
       var genreEntity = genreService.findGenreByName(genre)
               .orElseThrow(()->new NoSuchElementException("Жанр не найден"));
        return gameRepository.findByGenre(genreEntity,pageable);
    }


    @Override
    public Optional<GameEntity> findByGameName(String name) {
        return  gameRepository.findByGameName(name);
    }

    @Override
    public Page<GameEntity> allGamesWithPaging(PageRequest pageRequest) {
        return gameRepository.findAll(pageRequest);
    }


}
