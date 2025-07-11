package com.example.GameREST.Service.GeneraLogic.Validators.Factory;

import com.example.GameREST.Service.GeneraLogic.Validators.Impl.DeleteGeneralValidator;
import com.example.GameREST.Service.GeneraLogic.Validators.Impl.UpdateGeneralValidator;

public interface GeneralValidatorFactory<T>
{
     UpdateGeneralValidator<T> getUpdateValidator(Class<T> entityClass);
     DeleteGeneralValidator<T> getDeleteValidator(Class<T> entityClass);
}
