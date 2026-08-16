package com.rakshith.JobApplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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

        System.out.println("=================================");
        System.out.println("JWT Filter Executed");
        System.out.println("Request: " + request.getMethod()
                + " " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization Header: " + authHeader);

        /*
         * Step 1:
         * Check whether Authorization header exists
         * and starts with "Bearer "
         */
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            /*
             * Step 2:
             * Remove "Bearer " and get only the JWT
             */
            String jwt = authHeader.substring(7);

            System.out.println("JWT Token: " + jwt);

            /*
             * Step 3:
             * Validate JWT
             */
            boolean valid = jwtUtil.validateToken(jwt);

            System.out.println("Is Token Valid: " + valid);

            /*
             * Step 4:
             * Only authenticate the user if JWT is valid
             */
            if (valid) {

                /*
                 * Step 5:
                 * Extract username and role from JWT
                 */
                String username = jwtUtil.extractUsername(jwt);
                String role = jwtUtil.extractRole(jwt);

                System.out.println("Username: " + username);
                System.out.println("Role: " + role);

                /*
                 * Step 6:
                 * Create Spring Security Authentication
                 */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(role)
                                )
                        );

                /*
                 * Step 7:
                 * Store authentication in SecurityContext
                 */
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println("Authentication successfully set.");

            } else {

                System.out.println("Invalid JWT. Authentication not set.");
            }

        } else {

            System.out.println("No Bearer token found.");
        }

        /*
         * Step 8:
         * Continue the request
         */
        filterChain.doFilter(request, response);

        System.out.println("=================================");
    }
}