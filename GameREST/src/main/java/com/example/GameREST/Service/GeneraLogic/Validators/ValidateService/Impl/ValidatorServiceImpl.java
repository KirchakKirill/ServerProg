package com.example.GameREST.Service.GeneraLogic.Validators.ValidateService.Impl;

import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;
import com.example.GameREST.Service.GeneraLogic.Validators.Factory.GeneralValidatorFactory;
import com.example.GameREST.Service.GeneraLogic.Validators.GeneralValidator;
import com.example.GameREST.Service.GeneraLogic.Validators.ValidateService.ValidatorService;
import org.springframework.stereotype.Component;

@Component
public class ValidatorServiceImpl<T> implements ValidatorService<T>
{
    private final GeneralValidatorFactory<T> validatorFactory;

    public ValidatorServiceImpl(GeneralValidatorFactory<T> validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @Override
    public void validateUpdate(ValidateContext<T> validateContext) {
       GeneralValidator<T> platformGeneralValidator = validatorFactory.getUpdateValidator(validateContext.entityClass());
       platformGeneralValidator.validate(validateContext);
    }

    @Override
    public void validateDelete(ValidateContext<T> validateContext) {
        GeneralValidator<T> platformGeneralValidator = validatorFactory.getDeleteValidator(validateContext.entityClass());
        platformGeneralValidator.validate(validateContext);
    }
}
