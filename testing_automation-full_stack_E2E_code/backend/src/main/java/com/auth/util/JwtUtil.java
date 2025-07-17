package com.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        // Using UTF-8 explicitly for consistent key bytes conversion
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

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

    public String getEmailFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        Claims claims = parseClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public Long getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        Claims claims = parseClaims(token);
        if (claims == null) {
            return null;
        }
        // get returns Integer or Long depending on how it was set, handle carefully
        Object userIdClaim = claims.get("userId");
        if (userIdClaim instanceof Integer) {
            return ((Integer) userIdClaim).longValue();
        } else if (userIdClaim instanceof Long) {
            return (Long) userIdClaim;
        } else {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Could log the exception here if logging is set up
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return true;
        }
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // Could log the exception here if logging is set up
            return null;
        }
    }
}