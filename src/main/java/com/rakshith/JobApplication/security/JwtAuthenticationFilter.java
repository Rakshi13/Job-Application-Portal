package com.rakshith.JobApplication.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JWT Filter Executed");
        String authHeader =
                request.getHeader("Authorization");

        System.out.println(authHeader);

        if(authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);

            System.out.println("JWT Token:");
            System.out.println(jwt);

            boolean valid = jwtUtil.validateToken(jwt);

            System.out.println("Is Token Valid: " + valid);

            String username = jwtUtil.extractUsername(jwt);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.emptyList()
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            System.out.println("Username: " + username);
        }

        filterChain.doFilter(request, response);
    }
}
