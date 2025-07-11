package com.example.GameREST.Service.GeneraLogic.Validators;

import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;

public interface GeneralValidator<T>
{
    void validate  (ValidateContext<T> validateContext);
}
