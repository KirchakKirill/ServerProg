package com.example.GameREST.Service.GameService.Factories;

import com.example.GameREST.Service.GameService.Validators.GameValidator;

public interface GameValidatorProvider
{
    GameValidator getGameValidator(Class<? extends GameValidator> gameValidator);
}
