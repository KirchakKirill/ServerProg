package com.example.GameREST.Controller;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.JwtAuthenticationCookie.Token;
import com.example.GameREST.JwtAuthenticationCookie.TokenCookieSessionAuthenticationStrategy;
import com.example.GameREST.JwtAuthenticationCookie.TokenUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("games/api")
public class LoginController
{

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy;

    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        tokenCookieSessionAuthenticationStrategy.onAuthentication(authentication,request,response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {

    }



}
