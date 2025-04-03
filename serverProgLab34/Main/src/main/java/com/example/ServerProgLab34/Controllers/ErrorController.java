package com.example.ServerProgLab34.Controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(NoResourceFoundException.class)
    public String handle404(NoResourceFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "404";
    }
}
