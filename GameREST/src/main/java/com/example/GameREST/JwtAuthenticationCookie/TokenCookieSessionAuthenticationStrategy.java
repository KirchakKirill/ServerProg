package com.example.GameREST.JwtAuthenticationCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;

@Setter
@Slf4j
public class TokenCookieSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private Function<Authentication, Token> tokenCookieFactory = new DefaultTokenCookieFactory();
    private Function<Token,String> tokenStringSerializer = Objects::toString;

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {

            log.info("Strategy:"+authentication);
            if(authentication!=null && authentication.isAuthenticated()) {
                var token = this.tokenCookieFactory.apply(authentication);
                var tokenString = this.tokenStringSerializer.apply(token);

                var cookie = new Cookie("auth-token", tokenString);
                cookie.setPath("/");
                cookie.setDomain(null);
                cookie.setSecure(false);
                cookie.setHttpOnly(true);
                cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), token.getExpiresAt()));

                response.addCookie(cookie);
            }


    }
}
