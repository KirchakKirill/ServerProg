package com.example.GameREST.JwtAuthenticationCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;
import java.util.stream.Stream;


public class TokenCookieAuthenticationConverter implements AuthenticationConverter {


    private final Function<String, Token> tokenCookieStringDeserializer;

    public TokenCookieAuthenticationConverter(Function<String, Token> tokenCookieStringDeserializer) {
        this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if(request.getCookies() !=null )
        {

            System.out.println("Полученные куки:");
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + "=" + cookie.getValue());
            }

            return Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("__Host-auth-token"))
                    .findFirst()
                    .map(cookie -> {
                        var token = this.tokenCookieStringDeserializer.apply(cookie.getValue());
                        return new PreAuthenticatedAuthenticationToken(token,cookie.getValue());
                    })
                    .orElse(null);

        }
        return null;
    }
}
