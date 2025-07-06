package com.example.GameREST.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniqueConstraintViolationException extends RuntimeException
{
    private String message;
    private String entityName;
    private Long fieldId;
    private String duplicateValue;

    public UniqueConstraintViolationException(String message,String entityName,Long fieldId,String duplicateValue)
    {
        this.message = message;
        this.duplicateValue = duplicateValue;
        this.entityName = entityName;
        this.fieldId = fieldId;
    }
}
