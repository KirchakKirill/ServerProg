package com.example.GameREST.Service.GeneraLogic.Validators.Impl;

import com.example.GameREST.Repository.GeneralRepository;


public class DeleteGeneralValidator<T> extends AbstractGeneralValidator<T>
{
    private final GeneralRepository<T,Long> generalRepository;

    public DeleteGeneralValidator(GeneralRepository<T, Long> generalRepository) {
        this.generalRepository = generalRepository;
    }

    @Override
    Integer CounterLinksForEntity(T entity) {
        return generalRepository.countLinksForEntity(entity);
    }

    @Override
    String getBusinessLogicException(Integer count) {
        return "Сущность имеет  " + count + " связей" +
                "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения." +
                "Если уверенны, то forceDelete = true";
    }
}
