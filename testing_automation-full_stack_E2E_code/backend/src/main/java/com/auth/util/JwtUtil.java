package com.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySuperSecretKeyForJWTTokenGenerationAndValidationWithEnoughLength}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        final String key = jwtSecret != null ? jwtSecret.trim() : "";
        if (key.length() < 32) {
            throw new IllegalStateException("JWT secret key must be at least 32 characters long for HS512");
        }
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(final String email, final Long userId) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromToken(final String token) {
        final Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public Long getUserIdFromToken(final String token) {
        final Claims claims = parseClaims(token);
        final Object userIdObj = claims.get("userId");
        if (userIdObj == null) {
            return null;
        }
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        if (userIdObj instanceof String) {
            try {
                return Long.parseLong((String) userIdObj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public boolean validateToken(final String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log exception if logging is available (e.g., Logger.debug("Invalid JWT token: {}", e.getMessage()))
            return false;
        }
    }

    public boolean isTokenExpired(final String token) {
        try {
            final Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Consider token expired if any exception occurs during parsing
            return true;
        }
    }
}