package com.example.GameREST.Exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException{

    String message;

    public EntityAlreadyExistsException(String msg)
    {
        this.message = msg;
    }
}
