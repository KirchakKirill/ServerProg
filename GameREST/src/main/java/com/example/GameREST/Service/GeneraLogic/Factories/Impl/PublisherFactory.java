package com.example.GameREST.Service.GeneraLogic.Factories.Impl;

import com.example.GameREST.Entity.PublisherEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class PublisherFactory implements EntityFactory<PublisherEntity> {
    @Override
    public PublisherEntity build(CreateData<PublisherEntity> createData) {
        return PublisherEntity.builder()
                .publisherName(createData.entityName())
                .build();
    }

    @Override
    public Class<PublisherEntity> getEntityType() {
        return PublisherEntity.class;
    }
}
