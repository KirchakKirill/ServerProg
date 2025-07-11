package com.example.GameREST.Service.GeneraLogic.Factories.Impl;

import com.example.GameREST.Entity.GenreEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class GenreFactory implements EntityFactory<GenreEntity>
{
    @Override
    public GenreEntity build(CreateData<GenreEntity> createData) {
        return GenreEntity.builder()
                .genreName(createData.entityName())
                .build();
    }

    @Override
    public Class<GenreEntity> getEntityType() {
        return GenreEntity.class;
    }
}
