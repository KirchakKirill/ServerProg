package com.example.GameREST.Configuration;


import com.example.GameREST.Entity.UserAuthority;
import com.example.GameREST.JwtAuthenticationCookie.*;
import com.example.GameREST.Repository.DeactivatedTokenRepository;
import com.example.GameREST.Repository.UserRepository;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey,
            DeactivatedTokenRepository deactivatedTokenRepository,
            PreAuthenticatedAuthenticationProvider jwtAuthProvider) throws Exception {
        return new TokenCookieAuthenticationConfigurer()
                .authenticationTokenCookieDeserializer(new TokenCookieJweDeserializer(
                        new DirectDecrypter(OctetSequenceKey.parse(cookieTokenKey))
                ))
                .deactivatedTokenRepository(deactivatedTokenRepository)
                .jwtAuthProvider(jwtAuthProvider);
    }

    @Bean
    public TokenCookieJweSerializer tokenCookieJweStringSerializer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey
    ) throws Exception {
        return new TokenCookieJweSerializer(new DirectEncrypter(
                OctetSequenceKey.parse(cookieTokenKey)
        ));
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    @Bean
    public PreAuthenticatedAuthenticationProvider jwtAuthProvider(
            DeactivatedTokenRepository deactivatedTokenRepository)
    {
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService(deactivatedTokenRepository));
        return authenticationProvider;
    }

    @Bean
    public TokenCookieSessionAuthenticationStrategy tokenAuthStrategy(
            TokenCookieJweSerializer serializer) {
        var strategy = new TokenCookieSessionAuthenticationStrategy();
        strategy.setTokenStringSerializer(serializer);
        return strategy;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
            TokenCookieJweSerializer tokenCookieJweStringSerializer,
                AuthenticationManager authenticationManager) throws Exception {
        var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();
        tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);

        http.httpBasic(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST,"/games/api/login").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers(HttpMethod.POST,"/games/api/register").permitAll()
                                .requestMatchers("/games/api/game-name").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);



        return http.with(tokenCookieAuthenticationConfigurer,customer ->{}).build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {

        return username -> userRepository.findByUserName(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getAuthorities()
                                .stream().map(UserAuthority::getAuthority)
                                .map(SimpleGrantedAuthority::new)
                                .toList())
                        .build())
                .orElse(null);
    }


    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider daoAuthenticationProvider,
            PreAuthenticatedAuthenticationProvider jwtAuthProvider) throws Exception {



        return new ProviderManager(daoAuthenticationProvider,jwtAuthProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
