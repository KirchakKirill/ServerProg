package com.example.GameREST.Service.GeneraLogic.Validators.ValidateService;

import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;

public interface ValidatorService<T>
{
    void validateUpdate(ValidateContext<T> validateContext);
    void validateDelete(ValidateContext<T> validateContext);
}
