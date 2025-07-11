package com.example.GameREST.Service.GeneraLogic.Factories.Impl;


import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;
import com.example.GameREST.Service.GeneraLogic.Factories.GeneralEntityProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeneralEntityProviderImpl<T> implements GeneralEntityProvider<T>
{
    private final Map<Class<T>,EntityFactory<T>> factoryMap;

    public GeneralEntityProviderImpl(List<EntityFactory<T>> factories) {
        this.factoryMap = factories
                .stream()
                .collect(Collectors.toMap(
                        EntityFactory::getEntityType,
                        factory -> factory,
                        (exist,repl) -> exist
                ));
    }


    @Override
    @SuppressWarnings("unchecked")
    public <V extends EntityFactory<T>> V getEntityFactory(Class<T> entityClass) {
        EntityFactory<T> factory = factoryMap.get(entityClass);
        if (factory == null)
        {
            throw  new IllegalArgumentException("Фабрика для этой сущности не найдена");
        }
        return (V) factory;
    }
}
