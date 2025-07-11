package com.example.GameREST.Service.GameService.Factories.Impl;

import com.example.GameREST.Service.GameService.Factories.GameValidatorProvider;
import com.example.GameREST.Service.GameService.Validators.GameValidator;
import com.example.GameREST.Service.GameService.Validators.Impl.DeleteGameValidator;
import com.example.GameREST.Service.GameService.Validators.Impl.UpdateGameValidator;
import org.springframework.stereotype.Component;

@Component
public class GameValidatorProviderImpl implements GameValidatorProvider
{
    private final UpdateGameValidator updateGameValidator;
    private final DeleteGameValidator deleteGameValidator;

    public GameValidatorProviderImpl(UpdateGameValidator updateGameValidator, DeleteGameValidator deleteGameValidator) {
        this.updateGameValidator = updateGameValidator;
        this.deleteGameValidator = deleteGameValidator;
    }

    @Override
    public GameValidator getGameValidator(Class<? extends GameValidator> gameValidator) {

        if (gameValidator == UpdateGameValidator.class)
        {
            return updateGameValidator;
        } else if (gameValidator == DeleteGameValidator.class) {
            return  deleteGameValidator;
        }
        throw new IllegalArgumentException("Неизвестный тип валидатора: " + gameValidator);
    }
}
