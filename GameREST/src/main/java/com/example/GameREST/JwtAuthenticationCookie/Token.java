package com.example.GameREST.JwtAuthenticationCookie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private UUID id;
    private String subject;
    List<String> authorities;
    Instant createdAt;
    Instant expiresAt;
}
