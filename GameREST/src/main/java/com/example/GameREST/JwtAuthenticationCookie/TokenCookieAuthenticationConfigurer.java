package com.example.GameREST.JwtAuthenticationCookie;

import com.example.GameREST.Entity.DeactivatedToken;
import com.example.GameREST.Repository.DeactivatedTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import com.example.GameREST.Configuration.SecurityConfiguration;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.function.Function;

@Setter
public class TokenCookieAuthenticationConfigurer extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {


    private Function<String, Token>  authenticationTokenCookieDeserializer;

    private DeactivatedTokenRepository deactivatedTokenRepository;

    private PreAuthenticatedAuthenticationProvider jwtAuthProvider;

    @Override
    public void init(HttpSecurity builder) throws Exception
    {
        builder.logout(logout ->logout
                .logoutUrl("/games/api/logout")
                .addLogoutHandler(new CookieClearingLogoutHandler("auth-token"))
                .addLogoutHandler(((request, response, authentication) -> {
                    if(authentication != null && authentication.getPrincipal() instanceof TokenUser user)
                    {
                           System.out.printf("Processing logout for: %s%n",authentication.getName());
                        DeactivatedToken diedToken  = DeactivatedToken.builder()
                                .id(user.getToken().getId())
                                .keepUntil(user.getToken().getExpiresAt())
                                .build();
                        deactivatedTokenRepository.save(diedToken);


                    }
                })).logoutSuccessHandler(((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                })));
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception
    {
        var cookieAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new TokenCookieAuthenticationConverter(this.authenticationTokenCookieDeserializer));
        cookieAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {});
        cookieAuthenticationFilter.setFailureHandler(new AuthenticationEntryPointFailureHandler(new Http403ForbiddenEntryPoint()));

        cookieAuthenticationFilter.setRequestMatcher(request -> {
            String uri = request.getRequestURI();
            System.out.println("Request URI: " + request.getRequestURI());
            return !uri.equals("/games/api/register") &&
                    !uri.equals("/games/api/login") &&
                    !uri.equals("/swagger-ui/index.html") &&
                    !uri.equals("/swagger-ui/**");
        });

        builder.addFilterAfter(cookieAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(jwtAuthProvider);


    }



    public TokenCookieAuthenticationConfigurer authenticationTokenCookieDeserializer(Function<String, Token> authenticationTokenCookieDeserializer)
    {
        this.authenticationTokenCookieDeserializer = authenticationTokenCookieDeserializer;
        return this;

    }

    public TokenCookieAuthenticationConfigurer deactivatedTokenRepository(
            DeactivatedTokenRepository deactivatedTokenRepository) {
        this.deactivatedTokenRepository = deactivatedTokenRepository;
        return this;
    }

    public TokenCookieAuthenticationConfigurer jwtAuthProvider(
            PreAuthenticatedAuthenticationProvider jwtAuthProvider) {
        this.jwtAuthProvider = jwtAuthProvider;
        return this;
    }

}
