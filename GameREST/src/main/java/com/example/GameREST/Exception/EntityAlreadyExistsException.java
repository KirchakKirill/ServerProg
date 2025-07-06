package com.example.GameREST.Exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException{

    String message;
    String entityName;
    Long idExistingEntity;

    public EntityAlreadyExistsException(String msg, String entityName, Long idExistingEntity)
    {
        this.message = msg;
        this.entityName = entityName;
        this.idExistingEntity = idExistingEntity;
    }
}
