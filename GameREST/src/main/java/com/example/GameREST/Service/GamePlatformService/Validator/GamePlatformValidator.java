package com.example.GameREST.Service.GamePlatformService.Validator;

import com.example.GameREST.Entity.GamePublisherEntity;
import com.example.GameREST.Entity.PlatformEntity;
import com.example.GameREST.Service.GamePlatformService.Context.UpdateContext;

public interface GamePlatformValidator
{
    void validateCreate(GamePublisherEntity gamePublisher, PlatformEntity platform);
    void validateUpdate(UpdateContext context);
}
