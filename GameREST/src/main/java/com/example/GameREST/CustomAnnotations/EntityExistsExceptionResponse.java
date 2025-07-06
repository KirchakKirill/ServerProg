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

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "500",
        description = "Entity Exists Exception",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                        name = "Пример ошибки 500",
                        value = """
                {
                    "status": 500,
                    "error": "Сущность с такими параметрами уже существует",
                    "message": "Вы пытаетесь вставить две разные сущности в одно id",
                    "timestamp": "2025-07-05T12:00:00Z"
                }""",
                        summary = "Серверная ошибка"
                )
        )
)
public @interface EntityExistsExceptionResponse {
}
