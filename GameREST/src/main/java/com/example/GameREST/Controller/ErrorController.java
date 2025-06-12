package com.example.GameREST.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import  org.springframework.validation.BindException;

import java.util.Locale;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException  bindException, Locale locale)
    {
        ProblemDetail problemDetail  = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                this.messageSource.getMessage("error.400.title", new Object[0],"error.400.title", locale));
        problemDetail.setProperty("errors", bindException.getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).toList());
        return ResponseEntity
                .badRequest().body(problemDetail);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale)
    {
        ProblemDetail problemDetail  = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                this.messageSource.getMessage(exception.getMessage(), new Object[0],exception.getMessage(), locale));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }


}
