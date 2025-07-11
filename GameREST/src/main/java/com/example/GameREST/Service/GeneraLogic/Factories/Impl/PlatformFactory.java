package com.example.GameREST.Service.GeneraLogic.Factories.Impl;

import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class PlatformFactory implements EntityFactory<PlatformEntity> {
    @Override
    public PlatformEntity build(CreateData<PlatformEntity> createData) {
        return PlatformEntity.builder()
                .platformName(createData.entityName())
                .build();
    }

    @Override
    public Class<PlatformEntity> getEntityType() {
        return PlatformEntity.class;
    }
}
