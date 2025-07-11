package com.example.GameREST.Service.GameService.Validators.Context;

import com.example.GameREST.Entity.GameEntity;



public record GameValidateContext(GameEntity game, boolean forceAction, String gameName){}
