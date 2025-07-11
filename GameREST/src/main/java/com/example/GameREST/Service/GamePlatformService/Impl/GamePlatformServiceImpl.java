package com.example.GameREST.Service.GamePlatformService.Impl;

import com.example.GameREST.DTO.RequestGamePlatformDTO;
import com.example.GameREST.DTO.RequestGamePlatformUpdateDTO;
import com.example.GameREST.Entity.*;
import com.example.GameREST.Repository.GamePlatformRepository;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.GamePlatformService.GamePlatformService;
import com.example.GameREST.Service.GamePlatformService.Validator.Impl.GamePlatformValidatorImpl;
import com.example.GameREST.Service.GamePlatformService.Factories.Impl.GamePlatformFactoryImpl;
import com.example.GameREST.Service.GamePlatformService.Strategies.UpdateStrategy;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GamePlatformServiceImpl implements GamePlatformService
{


    private final  GamePlatformRepository gamePlatformRepository;
    private final GamePlatformFactoryImpl gamePlatformFactoryImpl;
    private final List<UpdateStrategy> updateStrategies;
    private final GamePlatformValidatorImpl gamePlatformValidatorImpl;

    public GamePlatformServiceImpl(GamePlatformRepository gamePlatformRepository, GamePlatformFactoryImpl gamePlatformFactoryImpl, List<UpdateStrategy> updateStrategies, GamePlatformValidatorImpl gamePlatformValidatorImpl) {
        this.gamePlatformRepository = gamePlatformRepository;
        this.gamePlatformFactoryImpl = gamePlatformFactoryImpl;
        this.updateStrategies = updateStrategies;
        this.gamePlatformValidatorImpl = gamePlatformValidatorImpl;
    }

    @Override
    @Transactional
    public GamePlatformEntity save(RequestGamePlatformDTO requestGamePlatformDTO) {

        return gamePlatformRepository.save(gamePlatformFactoryImpl.createFromDto(requestGamePlatformDTO));
    }

    @Override
    public Page<GamePlatformEntity> getAll(Pageable pageable) {

        return gamePlatformRepository.findAll(pageable);
    }

    @Override
    public Optional<GamePlatformEntity> findById(Long id) {
        return gamePlatformRepository.findById(id);
    }

    @Override
    @Transactional
    public void update(Long id , RequestGamePlatformUpdateDTO updateDTO){

        GamePlatformEntity gamePlatformEntity = gamePlatformRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("GamePlatform не найден"));
        UpdateContext context = UpdateContext.builder()
                        .dto(updateDTO)
                        .gamePlatformEntity(gamePlatformEntity)
                        .build();

        context.setUpEntitiesForUpdate();
        updateStrategies.forEach(s -> s.update(context));
        gamePlatformValidatorImpl.validateUpdate(context);
        gamePlatformRepository.update(gamePlatformEntity.getId(),context.getGamePublisher(),context.getPlatform(), updateDTO.getReleaseYear());
    }

    @Override
    public void delete(Long id) {
        gamePlatformRepository.deleteById(id);
    }

}
