package com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.Impl;

import com.example.GameREST.Repository.GeneralRepository;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.CreationPolicy;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;

import java.util.Optional;

public class LinkedGeneralCreationPolicy<T> implements CreationPolicy<T>
{
    private final GeneralRepository<T,Long> repository;
    private final EntityFactory<T> entityFactory;

    public LinkedGeneralCreationPolicy(GeneralRepository<T, Long> repository, EntityFactory<T> factory) {
        this.repository = repository;
        this.entityFactory = factory;
    }

    @Override
    public T apply(CreateData<T> createData) {
        Optional<T> entity  = repository.findEntityByName(createData.entityName());
        return entity.orElseGet(()->entityFactory.build(createData));
    }
}
