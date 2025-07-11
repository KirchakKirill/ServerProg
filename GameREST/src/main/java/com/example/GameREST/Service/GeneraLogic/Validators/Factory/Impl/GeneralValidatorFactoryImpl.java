package com.example.GameREST.Service.GeneraLogic.Validators.Factory.Impl;


import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Repository.GeneralRepository;
import com.example.GameREST.Service.GeneraLogic.Validators.Factory.GeneralValidatorFactory;
import com.example.GameREST.Service.GeneraLogic.Validators.Impl.DeleteGeneralValidator;
import com.example.GameREST.Service.GeneraLogic.Validators.Impl.UpdateGeneralValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GeneralValidatorFactoryImpl<T> implements GeneralValidatorFactory<T>
{
    private final Map<Class<?>, GeneralRepository<?,Long>> repositoryMap;

    public GeneralValidatorFactoryImpl(@Qualifier("genreRepository") GeneralRepository<GenreEntity,Long> genreRepository,
                                       @Qualifier("publisherRepository")GeneralRepository<PublisherEntity,Long> publisherRepository,
                                       @Qualifier("platformRepository")GeneralRepository<PlatformEntity,Long> platformRepository,
                                       @Qualifier("regionRepository")GeneralRepository<RegionEntity,Long> regionRepository)
    {
        this.repositoryMap = Map.of(GenreEntity.class,genreRepository,
                PublisherEntity.class,publisherRepository,
                PlatformEntity.class,platformRepository,
                RegionEntity.class,regionRepository);
    }

    @SuppressWarnings("unchecked")
    public UpdateGeneralValidator<T> getUpdateValidator(Class<T> entityClass) {
        GeneralRepository<T, Long> repo = (GeneralRepository<T, Long>) repositoryMap.get(entityClass);
        return new UpdateGeneralValidator<>(repo);
    }

    @SuppressWarnings("unchecked")
    public DeleteGeneralValidator<T> getDeleteValidator(Class<T> entityClass) {
        GeneralRepository<T, Long> repo = (GeneralRepository<T, Long>) repositoryMap.get(entityClass);
        return new DeleteGeneralValidator<>(repo);
    }
}
