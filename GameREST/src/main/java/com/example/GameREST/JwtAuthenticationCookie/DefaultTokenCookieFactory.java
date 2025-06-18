package com.example.GameREST.JwtAuthenticationCookie;

import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Setter
public class DefaultTokenCookieFactory implements Function<Authentication, Token> {

    private Duration tokenTtl = Duration.ofDays(1);


    @Override
    public Token apply(Authentication authentication) {
        var now = Instant.now();
        return Token.builder()
                .id(UUID.randomUUID())
                .subject(authentication.getName())
                .authorities(authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .createdAt(now)
                .expiresAt(now.plus(tokenTtl))
                .build();


    }
}
