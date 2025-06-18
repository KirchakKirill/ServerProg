package com.example.GameREST.JwtAuthenticationCookie;

import com.example.GameREST.Repository.DeactivatedTokenRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    public TokenAuthenticationUserDetailsService(DeactivatedTokenRepository deactivatedTokenRepository) {
        this.deactivatedTokenRepository = deactivatedTokenRepository;

    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if(authenticationToken.getPrincipal() instanceof Token token)
        {
            boolean isTokenValid = !deactivatedTokenRepository.existsById(token.getId()) && token.getExpiresAt().isAfter(Instant.now());
            return new TokenUser(token.getSubject(),"nopassword",true,true, isTokenValid,true,
                    token.authorities.stream().map(SimpleGrantedAuthority::new).toList(),token);
        }
        throw new UsernameNotFoundException("Principal должен иметь тип Token");
    }
}
