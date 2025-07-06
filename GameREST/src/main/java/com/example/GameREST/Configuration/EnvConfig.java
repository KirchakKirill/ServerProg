package com.example.GameREST.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @Bean
    public String accessTokenKey()
    {
        return System.getenv("JWT_ACCESS_TOKEN_KEY");
    }
    @Bean
    public String refreshTokenKey()
    {
        return System.getenv("JWT_REFRESH_TOKEN_KEY");
    }
    @Bean
    public String cookieTokenKey()
    {
        return System.getenv("JWT_COOKIE_TOKEN_KEY");
    }

    @Bean
    public String dbPassword()
    {
        return System.getenv("DB_PASSWORD");
    }

    @Bean
    public  String dbUserName()
    {
        return System.getenv("DB_USERNAME");
    }

}
