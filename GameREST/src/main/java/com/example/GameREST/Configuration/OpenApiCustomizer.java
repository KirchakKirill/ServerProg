package com.example.GameREST.Configuration;

import com.example.GameREST.CustomAnnotations.CustomServerError;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiCustomizer
{
    @Bean
    public OperationCustomizer customizeErrorResponses() {
        return (operation, handlerMethod) -> {
            CustomServerError annotation = handlerMethod.getMethodAnnotation(CustomServerError.class);
            if (annotation != null && !annotation.detail().isEmpty()) {
                Content content = operation.getResponses().get("500").getContent();
                MediaType mediaType = content.get("application/problem+json");

                Example example = new Example();
                example.setValue(String.format("""
                    {
                      "type": "about:blank",
                      "title": "%s",
                      "status": 500,
                      "detail": "%s"
                    }
                    """,
                                annotation.value().isEmpty() ? "Internal Server Error" : annotation.value(),
                                annotation.detail()));
                mediaType.addExamples("Error example",example);

            }
            return operation;
        };
    }
}
