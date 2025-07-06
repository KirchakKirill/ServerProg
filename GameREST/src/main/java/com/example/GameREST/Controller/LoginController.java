package com.example.GameREST.Controller;

import com.example.GameREST.DTO.UserDTO;
import com.example.GameREST.JwtAuthenticationCookie.Token;
import com.example.GameREST.JwtAuthenticationCookie.TokenCookieSessionAuthenticationStrategy;
import com.example.GameREST.JwtAuthenticationCookie.TokenFactoryLogin;
import com.example.GameREST.JwtAuthenticationCookie.TokenUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("games/api")
@Slf4j
public class LoginController
{


    private final  AuthenticationManager authenticationManager;


    private final TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy;

    private final Function<String,Token> tokenFactory  = new TokenFactoryLogin();

    public LoginController(AuthenticationManager authenticationManager, TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy) {
        this.authenticationManager = authenticationManager;
        this.tokenCookieSessionAuthenticationStrategy = tokenCookieSessionAuthenticationStrategy;
    }


    @Operation(
            summary = "Аутентификация пользователя",
            description = "Выполняет вход пользователя в систему и устанавливает аутентификационные cookie",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Учетные данные пользователя",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      "username": "existingUser",
                      "password": "userPassword123"
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная аутентификация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "message": "Аутентификация прошла успешно"
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public  ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response)
    {
        Token defaultToken  = tokenFactory.apply(userDTO.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new PreAuthenticatedAuthenticationToken(
                        defaultToken,
                        userDTO.getPassword()
                )
        );

        log.info(String.valueOf(authentication));
        tokenCookieSessionAuthenticationStrategy.onAuthentication(authentication,request,response);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Выход из системы",
            description = "Завершает сеанс пользователя и очищает аутентификационные cookie",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный выход из системы"
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.noContent().build();
    }



}
