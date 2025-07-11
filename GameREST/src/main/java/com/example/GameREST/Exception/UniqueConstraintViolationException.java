package com.example.GameREST.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniqueConstraintViolationException extends RuntimeException
{
    private String message;
    private Long fieldId;
    private String duplicateValue;

    public UniqueConstraintViolationException(String message,Long fieldId,String duplicateValue)
    {
        this.message = message;
        this.duplicateValue = duplicateValue;
        this.fieldId = fieldId;
    }
}
