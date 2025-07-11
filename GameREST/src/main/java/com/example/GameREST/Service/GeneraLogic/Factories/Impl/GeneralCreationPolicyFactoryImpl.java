package com.example.GameREST.Service.GeneraLogic.Factories.Impl;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Repository.GeneralRepository;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.CreationPolicy;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.Impl.LinkedGeneralCreationPolicy;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.Impl.StrictGeneralCreationPolicy;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;
import com.example.GameREST.Service.GeneraLogic.Factories.GeneralCreationPolicyFactory;
import com.example.GameREST.Service.GeneraLogic.Factories.GeneralEntityProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GeneralCreationPolicyFactoryImpl<T> implements GeneralCreationPolicyFactory<T> {

    private final Map<Class<?>, GeneralRepository<?,Long>> repositoryMap;
    private final GeneralEntityProvider<T> generalEntityProvider;

    public GeneralCreationPolicyFactoryImpl(@Qualifier("genreRepository") GeneralRepository<GenreEntity,Long> genreRepository,
                                            @Qualifier("publisherRepository")GeneralRepository<PublisherEntity,Long> publisherRepository,
                                            @Qualifier("platformRepository")GeneralRepository<PlatformEntity,Long> platformRepository,
                                            @Qualifier("regionRepository")GeneralRepository<RegionEntity,Long> regionRepository,
                                            GeneralEntityProvider<T> generalEntityProvider)
    {
        this.generalEntityProvider = generalEntityProvider;
        this.repositoryMap  = Map.of(GenreEntity.class,genreRepository,
                PublisherEntity.class,publisherRepository,
                PlatformEntity.class,platformRepository,
                RegionEntity.class,regionRepository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreationPolicy<T> getLinkedCreationPolicy(Class<T> entityClass) {
        GeneralRepository<T,Long> repository = (GeneralRepository<T, Long>) repositoryMap.get(entityClass);
        EntityFactory<T> factory  =  generalEntityProvider.getEntityFactory(entityClass);
        return new LinkedGeneralCreationPolicy<>(repository,factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreationPolicy<T> getStrictCreationPolicy(Class<T> entityClass) {
        GeneralRepository<T,Long> repository = (GeneralRepository<T, Long>) repositoryMap.get(entityClass);
        EntityFactory<T> factory  =  generalEntityProvider.getEntityFactory(entityClass);
        return new StrictGeneralCreationPolicy<>(repository,factory);
    }
}
