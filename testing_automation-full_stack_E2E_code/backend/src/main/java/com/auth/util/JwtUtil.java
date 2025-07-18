package com.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long jwtExpiration;

    /**
     * Get secret key for signing JWT.
     * Uses UTF-8 encoding for consistent key bytes.
     * Falls back to default if secret is blank.
     */
    private SecretKey getSigningKey() {
        String key = (jwtSecret == null || jwtSecret.isBlank()) ? 
                      "mySecretKeyForJWTTokenGenerationAndValidation" : jwtSecret;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Build JwtParser instance with signing key.
     */
    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }

    /**
     * Generate JWT token with email and userId claims.
     */
    public String generateToken(String email, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract email (subject) from JWT token.
     */
    public String getEmailFromToken(String token) {
        Claims claims = getJwtParser()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Extract userId claim from JWT token.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getJwtParser()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    /**
     * Validate token signature and expiration.
     */
    public boolean validateToken(String token) {
        try {
            getJwtParser().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Could log the exception here if a logger is added
            return false;
        }
    }

    /**
     * Check if the token is expired.
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getJwtParser()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}