package com.example.GameREST.JwtAuthenticationCookie;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.function.Function;

@Setter
public class TokenCookieJweSerializer implements Function<Token,String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCookieJweSerializer.class);

    private final JWEEncrypter jweEncrypter;

    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    public TokenCookieJweSerializer(JWEEncrypter jweEncrypter) {
        this.jweEncrypter = jweEncrypter;
    }

    public TokenCookieJweSerializer(JWEEncrypter jweEncrypter,JWEAlgorithm jweAlgorithm,EncryptionMethod encryptionMethod ) {
        this.jweEncrypter = jweEncrypter;
        this.jweAlgorithm = jweAlgorithm;
        this.encryptionMethod = encryptionMethod;
    }

    @Override
    public String apply(Token token) {
     var jweHeader = new JWEHeader.Builder(this.jweAlgorithm,this.encryptionMethod)
             .keyID(token.getId().toString())
             .build();
     var claimsSet = new JWTClaimsSet.Builder()
             .jwtID(token.getId().toString())
             .subject(token.getSubject())
             .issueTime(Date.from(token.getCreatedAt()))
             .expirationTime(Date.from(token.getExpiresAt()))
             .claim("authorities",token.getAuthorities())
             .build();
        var encryptedJWT = new EncryptedJWT(jweHeader, claimsSet);
        try
        {
            encryptedJWT.encrypt(this.jweEncrypter);
            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }
}
