package com.example.GameREST.Service.GeneraLogic.Factories.Impl;

import com.example.GameREST.Entity.RegionEntity;
import com.example.GameREST.Service.GeneraLogic.CreateData.CreateData;
import com.example.GameREST.Service.GeneraLogic.Factories.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class RegionFactory implements EntityFactory<RegionEntity> {
    @Override
    public RegionEntity build(CreateData<RegionEntity> createData) {
        return RegionEntity.builder()
                .regionName(createData.entityName())
                .build();
    }

    @Override
    public Class<RegionEntity> getEntityType() {
        return RegionEntity.class;
    }
}
