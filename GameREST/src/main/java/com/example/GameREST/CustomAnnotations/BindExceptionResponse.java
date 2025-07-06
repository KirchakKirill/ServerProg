package com.example.GameREST.CustomAnnotations;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(value = """
  {
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Неверные данные или формат данных",
  "instance": "/games/api/platform/update/1",
  "errors": [
    "длина должна находиться в диапазоне от 3 до 50"
  ]
}
                            """)
        )
)
public @interface BindExceptionResponse {
}
