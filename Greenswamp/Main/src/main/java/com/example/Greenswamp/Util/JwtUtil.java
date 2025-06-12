package com.example.Greenswamp.Util;


import com.example.Greenswamp.Data.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("s7F3jKp9Xy2BvE8hL5tRqW1mN4cZ7xYdA0zV6uIoP9gG2fJw3")
    private String secret;

    @Value("${jwt.expiration:604800000}")
    private long expirationTime;

    private SecretKey getSecretKey(){
        if (secret == null || secret.length() < 32){
            throw  new RuntimeException("Incorrect secret key");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserPrincipal userDetails){
        Map<String,Object> claims  =  new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());

        return createToken(claims,userDetails.getUsername());

    }

    public Long getExpirationTime(){
        return this.expirationTime;
    }


    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey())
                .compact();
    }

    private Claims getClaimsFromToken(String token)
    {
        try{
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось извлесь данные" + e);
        }

    }


    public <T> T getClaimsFromToken(String token, Function<Claims,T> claimsResolver)
    {

        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }


    public String getUserNameFromToken(String token){
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public  Boolean isTokenExpired(String token){
        return getExpirationDateFromToken(token).before(new Date());
    }

    public Date getExpirationDateFromToken(String token){

        return getClaimsFromToken(token,Claims::getExpiration);
    }



    public Boolean validateToken(String token, UserPrincipal userPrincipal){
        try {
            final String username  =  getUserNameFromToken(token);
            return  username.equals(userPrincipal.getUsername()) && !isTokenExpired(token);
        }
        catch (Exception e)
        {
            throw  new RuntimeException(e);
        }
    }

}
