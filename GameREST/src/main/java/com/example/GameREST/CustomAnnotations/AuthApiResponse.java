package com.example.GameREST.CustomAnnotations;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({

        @ApiResponse(
                responseCode = "401",
                description = "Не авторизован",
                content = @Content(
                        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                        schema = @Schema(implementation = ProblemDetail.class),
                        examples = @ExampleObject(value = """
                {
                  "type": "about:blank",
                  "title": "Unauthorized",
                  "status": 401,
                  "detail": "Full authentication is required to access this resource"
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Доступ запрещен - недостаточно прав",
                content = @Content(
                        mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                        schema = @Schema(implementation = ProblemDetail.class),
                        examples = @ExampleObject(value = """
                {
                  "type": "about:blank",
                  "title": "Forbidden",
                  "status": 403,
                  "detail": "Access Denied"
                }
            """)
                )
        )
})
public @interface AuthApiResponse
{

}
