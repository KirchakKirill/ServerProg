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
        description = "Ошибка: дублирование данных",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                        name = "Пример ошибки 409",
                        value = """
                {
                    "status": 409,
                    "error": "Entity already exists exception",
                    "message": "Не удалось выполнить операцию из-за конфликта уникальности",
                    "timestamp": "2025-07-05T12:00:00Z"
                }""",
                        summary = "Ошибка уникальности"
                )
        )
)
public @interface EntityAlreadyExistsExceptionResponse {
}
