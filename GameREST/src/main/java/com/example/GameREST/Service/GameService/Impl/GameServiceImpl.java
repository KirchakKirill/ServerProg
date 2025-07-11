package com.example.GameREST.Service.GameService.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.GameService.CreationPolicy.GameCreationPolicy;
import com.example.GameREST.Service.GameService.DataPackage.GamePackage;
import com.example.GameREST.Service.GameService.Factories.GameValidatorProvider;
import com.example.GameREST.Service.GameService.GameService;
import com.example.GameREST.Service.GameService.Validators.Context.GameValidateContext;
import com.example.GameREST.Service.GameService.Validators.GameValidator;
import com.example.GameREST.Service.GameService.Validators.Impl.DeleteGameValidator;
import com.example.GameREST.Service.GameService.Validators.Impl.UpdateGameValidator;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreationPolicyState;
import com.example.GameREST.Service.GenreService.GenreService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public  class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GenreService genreService;
    private final Map<CreationPolicyState, GameCreationPolicy> creationPolicyMap;
    private final GameValidatorProvider gameValidatorProvider;

    public GameServiceImpl(GameRepository gameRepository, GenreService genreService, Map<CreationPolicyState, GameCreationPolicy> creationPolicyMap, GameValidatorProvider gameValidatorProvider) {
        this.gameRepository = gameRepository;
        this.genreService = genreService;
        this.creationPolicyMap = creationPolicyMap;
        this.gameValidatorProvider = gameValidatorProvider;
    }

    @Override
    public Optional<GameEntity> findById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    @Transactional
    public GameEntity addNewGame(String gameName, GenreEntity genreAdd,CreationPolicyState creationPolicy)
    {
        GamePackage dataGameForCreate = GamePackage.builder()
                .gameName(gameName)
                .genre(genreAdd)
                .build();
        GameEntity createdGame = creationPolicyMap.get(creationPolicy).apply(dataGameForCreate);
        return  gameRepository.save(createdGame);
    }

    @Override
    @Transactional
    public void updateGame(Long id, String gameName, GenreEntity updateGenre,boolean forceUpdate)
    {
       GameEntity findGame = gameRepository.findById(id)
               .orElseThrow(()-> new NoSuchElementException("Игра не найдена"));
        GameValidateContext context = new GameValidateContext(findGame,forceUpdate,gameName);
        GameValidator gameValidator = gameValidatorProvider.getGameValidator(UpdateGameValidator.class);
        gameValidator.validate(context);
        gameRepository.updateGame(id,gameName,updateGenre);
    }


    @Override
    @Transactional
    public void deleteGame(Long id, boolean forceDelete) {
        GameEntity findGame= gameRepository.findById(id)
                        .orElseThrow(()->new NoSuchElementException("Игра не найдена"));

        GameValidateContext context = new GameValidateContext(findGame,forceDelete,null);
        GameValidator gameValidator = gameValidatorProvider.getGameValidator(DeleteGameValidator.class);
        gameValidator.validate(context);
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
