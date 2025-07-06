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
        responseCode = "428",
        description = "Ошибка бизнес логики",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                        name = "Пример ошибки бизнес логики",
                        value = """
                                {
                                    "status": 428,
                                    "error": "Сущность связана с 5 другими. Убедитесь, что изменение данной сущности не повлияет на работу вашего приложения. Если уверены, то forceUpdate = true",
                                    "message": "Ошибка бизнес логики",
                                    "timestamp": "2025-07-05T12:00:00Z"
                                }
                                """
                )
        )
)
public @interface BusinessLogicExceptionResponse {
}
