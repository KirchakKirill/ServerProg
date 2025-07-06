package com.example.GameREST.Exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException
{
    private final String message;

    public BusinessLogicException(String message)
    {
        this.message = message;
    }


}
