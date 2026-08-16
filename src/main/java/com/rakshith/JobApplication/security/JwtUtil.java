package com.rakshith.JobApplication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    // =========================
    // Generate JWT
    // =========================

    public String generateToken(String username, String role) {

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }


    // =========================
    // Extract Username
    // =========================

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }


    // =========================
    // Extract Role
    // =========================

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }


    // =========================
    // Extract Expiration
    // =========================

    public Date extractExpiration(String token) {

        return extractAllClaims(token)
                .getExpiration();
    }


    // =========================
    // Validate JWT
    // =========================

    public boolean validateToken(String token) {

        try {

            Claims claims = extractAllClaims(token);

            return !claims
                    .getExpiration()
                    .before(new Date());

        } catch (Exception e) {

            System.out.println("JWT validation failed: "
                    + e.getMessage());

            return false;
        }
    }


    // =========================
    // Extract Claims
    // =========================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // =========================
    // Signing Key
    // =========================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}