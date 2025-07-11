package com.example.GameREST.JwtAuthenticationCookie;

import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.Entity.UserEntity;
import com.example.GameREST.Repository.DeactivatedTokenRepository;
import com.example.GameREST.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final DeactivatedTokenRepository deactivatedTokenRepository;
    private final UserRepository userRepository;

    public TokenAuthenticationUserDetailsService(DeactivatedTokenRepository deactivatedTokenRepository, UserRepository userRepository) {
        this.deactivatedTokenRepository = deactivatedTokenRepository;

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException
    {
        if(authenticationToken.getPrincipal() instanceof Token token) {

            if (token.getAuthorities() == null) {
                String userName = token.getSubject();
                UserEntity user = userRepository.findByUserNameWithAuthorities(userName)
                        .orElseThrow(() -> new NoSuchElementException("Пользователь с таким именем не найден"));
                List<String> authorities = user.getAuthorities()
                        .stream()
                        .map(UserAuthority::getAuthority)
                        .toList();
                token.setAuthorities(authorities);
            }

            log.info("Я попал в userDetails: " + token);
            boolean isTokenValid = !deactivatedTokenRepository.existsById(token.getId()) && token.getExpiresAt().isAfter(Instant.now());
            return new TokenUser(token.getSubject(), "nopassword", true, true, isTokenValid, true,
                    token.authorities.stream().map(SimpleGrantedAuthority::new).toList(), token);
        }
        throw new UsernameNotFoundException("Principal должен иметь тип Token");
    }
}
