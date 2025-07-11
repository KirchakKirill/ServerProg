package com.example.GameREST.Service.GameService.Validators.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import com.example.GameREST.Repository.GameRepository;
import com.example.GameREST.Service.GameService.Validators.Context.GameValidateContext;
import org.springframework.stereotype.Component;

@Component
public class UpdateGameValidator extends AbstractGameValidator
{
    private final GameRepository gameRepository;

    public UpdateGameValidator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    Integer CounterLinksForGame(GameEntity game) {
        return gameRepository.existsWithGame(game);
    }

    GameEntity checkExistsGameBeforeUpdate(String gameName)
    {
        return gameRepository.findByGameName(gameName).orElse(null);
    }

    @Override
    String getBusinessLogicException(Integer count) {
        return "Игра имеет  " + count + " связей тип [Игра - Издатель]. " +
                "Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения. " +
                "Если уверенны, то forceUpdate = true";
    }

    @Override
    public void validate(GameValidateContext gameValidateContext)
    {
        super.validate(gameValidateContext);
        checkUniqueConstraintViolationException(gameValidateContext);
    }

    private void checkUniqueConstraintViolationException(GameValidateContext gameValidateContext)
    {
        GameEntity existing = checkExistsGameBeforeUpdate(gameValidateContext.gameName());
        if (existing != null)
        {
            throw new UniqueConstraintViolationException("Игра с таким названием уже существует",
                    existing.getId(),
                    existing.getGameName());
        }
    }
}
