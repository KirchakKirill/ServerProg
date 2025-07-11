package com.example.GameREST.Service.GameService.CreationPolicy;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Service.GameService.DataPackage.GamePackage;

public interface GameCreationPolicy
{
    GameEntity apply(GamePackage gameData);
}
