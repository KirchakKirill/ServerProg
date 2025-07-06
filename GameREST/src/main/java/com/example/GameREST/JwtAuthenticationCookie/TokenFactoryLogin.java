package com.example.GameREST.JwtAuthenticationCookie;

import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;


public class TokenFactoryLogin implements Function<String,Token> {

    private final Duration tokenTtl = Duration.ofDays(1);


    @Override
    public Token apply(String subjectName)
    {
        var now = Instant.now();
        return Token.builder()
                .id(UUID.randomUUID())
                .subject(subjectName)
                .authorities(null)
                .createdAt(now)
                .expiresAt(now.plus(tokenTtl))
                .build();

    }
}
