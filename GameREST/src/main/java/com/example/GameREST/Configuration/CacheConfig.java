package com.example.GameREST.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.HOURS);

        return new CaffeineCacheManager("gamesPageCache",
                "gamePlatformCache",
                "regionSalesCache");
    }
}
