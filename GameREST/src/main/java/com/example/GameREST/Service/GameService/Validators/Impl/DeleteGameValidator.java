package com.example.GameREST.Service.GameService.Validators.Impl;

import com.example.GameREST.Entity.GameEntity;
import com.example.GameREST.Repository.GameRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteGameValidator extends AbstractGameValidator
{
    private  final GameRepository gameRepository;

    public DeleteGameValidator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    Integer CounterLinksForGame(GameEntity game) {
        return gameRepository.existsWithGame(game);
    }

    @Override
    String getBusinessLogicException(Integer count) {
        return "Игра имеет  " + count + " связей тип [Игра - Издатель]. " +
                "Убедитесь, что удаление данной сущности не повлияет на работу вашего приложения. " +
                "Если уверенны, то forceUpdate = true";
    }
}
