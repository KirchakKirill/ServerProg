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
        responseCode = "400",
        description = "Ошибка валидации входных данных",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {@ExampleObject(
                        name = "Пример ошибки валидации",
                        value = """
                                {
                                    "status": 400,
                                    "error": "Ошибка валидации",
                                    "message": {
                                        "publisherName": "Длина названия должна быть от 3 до 50 символов"
                                    },
                                    "timestamp": "2025-07-05T12:00:00Z"
                                }""",

                        summary = "Некорректные данные"
                ),
                        @ExampleObject(
                                name = "Пример ошибки валидации №2",
                                value = """
                                    {
                                    "status": 400,
                                    "error": "Ошибка валидации",
                                    "message": {
                                        "id": "Значение должно быть не меньше единицы"
                                    },
                                    "timestamp": "2025-07-05T12:00:00Z"
                                }"""
                        )
                }
        )
)
public @interface ConstraintValidationExceptionResponse {
}
