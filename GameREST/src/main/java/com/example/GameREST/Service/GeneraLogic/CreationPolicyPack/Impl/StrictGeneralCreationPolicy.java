package com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.Impl;

import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Repository.GeneralRepository;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.CreationPolicyPack.CreationPolicy;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;

import java.util.Optional;

public class StrictGeneralCreationPolicy<T> implements CreationPolicy<T>
{
    private final GeneralRepository<T,Long> repository;
    private final EntityFactory<T> entityFactory;

    public StrictGeneralCreationPolicy(GeneralRepository<T, Long> repository, EntityFactory<T> entityFactory) {
        this.repository = repository;
        this.entityFactory = entityFactory;
    }

    @Override
    public T apply(CreateData<T> createData) {
        Optional<T> entity = repository.findEntityByName(createData.entityName());
        entity.ifPresent(e->{
            throw  new EntityAlreadyExistsException("Сущность с таким названием уже существует");
        });

        return entityFactory.build(createData);
    }
}
