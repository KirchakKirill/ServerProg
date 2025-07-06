package com.example.GameREST.Controller;

import com.example.GameREST.DTO.ErrorResponseDTO;
import com.example.GameREST.Exception.BusinessLogicException;
import com.example.GameREST.Exception.EntityAlreadyExistsException;
import com.example.GameREST.Exception.UniqueConstraintViolationException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class RestErrorController {


    private final MessageSource messageSource;

    public RestErrorController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ApiResponse(
            responseCode = "404",
            description = "Ресурс не найден",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(
                            name = "Пример ошибки 404",
                            value = """
                {
                    "status": 404,
                    "error": "Не найдено",
                    "message": "Издатель с id 123 не найден",
                    "timestamp": "2025-07-05T12:00:00Z"
                }""",
                            summary = "Ресурс отсутствует"
                    )

            )
    )
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NoSuchElementException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .error(ex.getMessage())
                .message("Not Found")
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ApiResponse(
            responseCode = "500",
            description = "Внутренняя ошибка сервера",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(
                            name = "Пример ошибки 500",
                            value = """
                {
                    "status": 500,
                    "error": "Внутренняя ошибка сервера",
                    "message": "Недопустимый аргумент",
                    "timestamp": "2025-07-05T12:00:00Z"
                }""",
                            summary = "Серверная ошибка"
                    )
            )
    )
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handle(IllegalArgumentException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .error(ex.getMessage())
                .message("Internal server error")
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityExistsException(EntityExistsException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .error("Сущность с такими параметрами уже существует")
                .message(ex.getMessage())
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }



    @Order(Ordered.LOWEST_PRECEDENCE)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(ConstraintViolationException ex) {

        ErrorResponseDTO errors = ErrorResponseDTO.builder()
                .error(ex.getMessage())
                .message("Constraint violation exception")
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(errors);
    }



    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException  bindException, Locale locale)
    {
        ProblemDetail problemDetail  = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                this.messageSource.getMessage("Неверные данные или формат данных", new Object[0],"Неверные данные или формат данных", locale));
        problemDetail.setProperty("errors", bindException.getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).toList());
        return ResponseEntity
                .badRequest().body(problemDetail);
    }



    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessLogicException(BusinessLogicException  businessLogicException)
    {
        ErrorResponseDTO errors = ErrorResponseDTO.builder()
                .error("Ошибка бизнес логики")
                .message(businessLogicException.getMessage())
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.PRECONDITION_REQUIRED.value())
                .build();

        return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(errors);

    }



    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleUniqueConstraintViolationException(UniqueConstraintViolationException  uniqueConstraintViolationException)
    {
        String entityName = uniqueConstraintViolationException.getEntityName();
        String duplicateValue = uniqueConstraintViolationException.getDuplicateValue();
        Long fieldId = uniqueConstraintViolationException.getFieldId();
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .error("Нарушение уникальности данных")
                .message(String.format(
                        "Значение '%s' уже существует для поля c Id = '%s' в сущности %s",
                        duplicateValue,
                        fieldId,
                        entityName
                ))
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

    }


    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .error("Конфликт данных")
                .message(String.format(
                        "%s; Сущность: %s; ID существующей записи: %d",

                        ex.getMessage(),
                        ex.getEntityName(),
                        ex.getIdExistingEntity()
                ))
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex)
    {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .error("Доступ запрещен")
                .message(ex.getMessage())
                .timestamp(Date.from(Instant.now()))
                .status(HttpStatus.FORBIDDEN.value())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}
