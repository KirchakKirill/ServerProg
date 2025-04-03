package com.example.ServerProgLab34.Controllers;


import com.example.ServerProgLab34.Data.ContactData;
import com.example.ServerProgLab34.Data.Subscribe;
import com.example.ServerProgLab34.Services.CsvExportService;
import com.example.ServerProgLab34.Services.SubscribeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {


    @Autowired
    private CsvExportService csvExportService;

    @Autowired
    private SubscribeService subscribeService;

    @ModelAttribute("contactData")
    public ContactData getContact(){
        return new ContactData();
    }

    @ModelAttribute("subscribe")
    public Subscribe getSubscribeData(){
        return new Subscribe("kirchak.kirya@gmail.ru");
    }



    @RequestMapping(method = RequestMethod.GET,value = "/home")
    public String homePage(){
        return "index";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @PostMapping("/contact")
    public String contactForm(@Valid @ModelAttribute("contactData")   ContactData data, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("errors",bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "contact";
        }
        else {
            try{
                csvExportService.writeToCsv(data);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return "redirect:/home";
        }
    }

    @GetMapping("/privacy")
    public String privacy(){
        return "privacy";
    }

    @GetMapping("/tos")
    public String tos(){
        return "tos";
    }
    @PostMapping("/subscribe")
    public String subscribe (@ModelAttribute Subscribe subscribe) {
        subscribeService.sendSubscriptionEmail(subscribe.email());

        return "redirect:/home";
    }


}
