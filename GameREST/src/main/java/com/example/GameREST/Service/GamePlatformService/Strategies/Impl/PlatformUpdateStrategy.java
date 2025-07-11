package com.example.GameREST.Service.GamePlatformService.Strategies.Impl;

import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;
import com.example.GameREST.Service.PlatformService.PlatformService;

import java.util.Optional;

public class PlatformUpdateStrategy extends AbstractEntityUpdateStrategy<PlatformEntity>
{
    private final PlatformService platformService;

    public PlatformUpdateStrategy(PlatformService platformService) {
        this.platformService = platformService;
    }


    @Override
    boolean needsUpdate(UpdateContext context) {
        return context.needsPlatformUpdate();
    }

    @Override
    Optional<PlatformEntity> findEntity(UpdateContext context) {
        return platformService.findPlatformByName(context.getDto().getPlatformName());
    }

    @Override
    void setEntity(UpdateContext context, PlatformEntity entity) {
        context.setPlatform(entity);
    }
}
