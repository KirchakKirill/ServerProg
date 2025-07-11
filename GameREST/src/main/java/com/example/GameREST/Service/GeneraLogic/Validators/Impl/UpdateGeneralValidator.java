package com.example.GameREST.Service.GeneraLogic.Validators.Impl;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.GeneralRepository;
import com.example.GameREST.Service.GeneraLogic.Validators.Context.ValidateContext;


public class UpdateGeneralValidator<T> extends AbstractGeneralValidator<T>
{
    private final GeneralRepository<T,Long> repository;

    public UpdateGeneralValidator(GeneralRepository<T,Long> repository) {
        this.repository = repository;
    }

    @Override
    Integer CounterLinksForEntity(T entity) {
        return repository.countLinksForEntity(entity);
    }

    @Override
    String getBusinessLogicException(Integer count) {
        return  "Сущность имеет  " + count + " связей" +
                "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения." +
                "Если уверенны, то forceUpdate = true";
    }

    @Override
    public void validate(ValidateContext<T> validateContext)
    {
        super.validate(validateContext);
        checkUniqueConstraintViolationException(validateContext);
    }

    private Long checkExistsGameBeforeUpdate(String entityName)
    {
        return repository.findIdByName(entityName).orElse(null);
    }

    private void checkUniqueConstraintViolationException(ValidateContext<T> validateContext)
    {
        Long existingId = checkExistsGameBeforeUpdate(validateContext.entityUpdateName());

        if (existingId != null)
        {
            throw new UniqueConstraintViolationException("Сущность с таким названием уже существует",
                    existingId,
                    validateContext.entityUpdateName());
        }
    }

}
