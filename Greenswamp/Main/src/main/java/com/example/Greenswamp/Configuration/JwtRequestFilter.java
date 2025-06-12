package com.example.Greenswamp.Configuration;


import com.example.Greenswamp.Data.UserPrincipal;
import com.example.Greenswamp.Services.UserService;
import com.example.Greenswamp.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

//        String requestURI = request.getRequestURI();
//        System.out.println("Processing request for: " + requestURI);


//        if (requestURI.startsWith("/account/") ||
//                requestURI.startsWith("/static/") ||
//                requestURI.equals("/home")) {
//            chain.doFilter(request, response);
//            return;
//        }

        String jwtToken = getJwtFromRequest(request);
//        System.out.println("JWT token: " + jwtToken);

        if (jwtToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No JWT token found");
            return;
        }
        if (jwtUtil.isTokenExpired(jwtToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token expired");
            return;
        }

        try {
            String username = jwtUtil.getUserNameFromToken(jwtToken);
//            System.out.println("Username from token: " + username);

            UserPrincipal userDetails = userService.loadUserByUsername(username);
//            System.out.println("User authorities: " + userDetails.getAuthorities());

//            System.out.println("\n\n" + jwtUtil.validateToken(jwtToken, userDetails));

            if (!jwtUtil.validateToken(jwtToken, userDetails)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token");
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.out.println("Error processing JWT: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error processing JWT");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> "JWT".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
