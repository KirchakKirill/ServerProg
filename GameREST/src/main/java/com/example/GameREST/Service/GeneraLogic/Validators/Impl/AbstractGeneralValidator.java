package com.example.GameREST.Service.GeneraLogic.Validators.Impl;

import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;
import com.example.GameREST.Service.GeneraLogic.Validators.GeneralValidator;

public abstract class AbstractGeneralValidator<T> implements GeneralValidator<T>
{

    abstract  Integer CounterLinksForEntity(T entity);
    abstract String getBusinessLogicException(Integer count);

    @Override
    public void validate(ValidateContext<T> validateContext)
    {
        Integer count  = CounterLinksForEntity(validateContext.entity());
        if (count > 0 && !validateContext.forceAction()) {
            throw new BusinessLogicException(getBusinessLogicException(count));
        }
    }
}
