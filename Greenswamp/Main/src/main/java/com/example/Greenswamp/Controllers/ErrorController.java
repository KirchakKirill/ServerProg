package com.example.Greenswamp.Controllers;

import com.example.Greenswamp.Data.Subscribe;
import com.example.Greenswamp.Services.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorController {
    @Autowired
    private SubscribeService subscribeService;

    @ModelAttribute("subscribe")
    public Subscribe getSubscribeData(){
        return new Subscribe();
    }
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public String handle404(NoResourceFoundException ex, Model model, @ModelAttribute Subscribe subscribe) {
        subscribeService.sendSubscriptionEmail(subscribe.getEmail());
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }
}
