package com.example.Greenswamp.Controllers;


import com.example.Greenswamp.Data.AuthData;
import com.example.Greenswamp.Data.UserData;
import com.example.Greenswamp.Data.UserPrincipal;
import com.example.Greenswamp.Entity.Authority;
import com.example.Greenswamp.Entity.User;
import com.example.Greenswamp.Services.AuthorityService;
import com.example.Greenswamp.Services.UserService;
import com.example.Greenswamp.Util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @ModelAttribute(name = "userData")
    public UserData initUserData(){
        return new UserData();
    }

    @ModelAttribute(name = "authData")
    public AuthData  initAuthData(){
        return new AuthData();
    }

    @GetMapping("/login")
    public  String login(){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute(name = "authData") AuthData authData, HttpServletResponse response ){

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authData.getUsername(), authData.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Long  userId =  userService.findByUsername(authData.getUsername())
                    .orElseThrow(()-> new UsernameNotFoundException("Пользователь не найден")).getId();



            UserPrincipal userPrincipal = userService.loadUserByUsername(authData.getUsername());
            String token = jwtUtil.generateToken(userPrincipal);

            System.out.println(userPrincipal.getAuthorities().stream().findFirst().get());


            Cookie jwtCookie = new Cookie("JWT", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge((int) (jwtUtil.getExpirationTime() / 1000)); // Синхронизируем с JWT
            response.addCookie(jwtCookie);

            return "redirect:/profile/" + userId;

        } catch (RuntimeException e) {
            System.out.println("ERROR ----------------------------------------------------------------");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register(){
        return  "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute(name = "userData") UserData userData, BindingResult bindingResult, Model model)
    {

        if (bindingResult.hasErrors())
        {
            model.addAttribute("errors", bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "register";
        }

        userService.saveUser(userData);

        return  "redirect:/home";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){

        Cookie jwtCookie = new Cookie("JWT", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }


}
