package com.example.GameREST.CustomAnnotations;


import com.example.GameREST.DTO.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "409",
        description = "Нарушение ограничения уникальности названия сущности",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                        name = "Пример ошибки бизнес логики",
                        value = """
                {
                    "status": 409,
                    "error": "Нарушение уникальности данных",
                    "message":"Значение RPG уже существует для поля c Id = 1 в сущности GenreEntity",
                    "timestamp": "2025-07-05T12:00:00Z"
                }"""
                )
        )
)
public @interface UniqueConstraintViolationExceptionResponse {
}
