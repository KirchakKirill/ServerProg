package com.example.GameREST.Service.GameService.Validators.Impl;


import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Service.GameService.Validators.Context.GameValidateContext;
import com.example.GameREST.Service.GameService.Validators.GameValidator;

public abstract class AbstractGameValidator implements GameValidator
{
    abstract  Integer CounterLinksForGame(GameEntity game);
    abstract String getBusinessLogicException(Integer count);
    @Override
    public void validate(GameValidateContext gameValidateContext)
    {
        Integer count  = CounterLinksForGame(gameValidateContext.game());
        if (count > 0 && !gameValidateContext.forceAction()) {
            throw new BusinessLogicException(getBusinessLogicException(count));
        }

    }





}
