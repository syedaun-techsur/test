package com.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        // Ensure the secret key meets minimum length for HS512 (at least 64 bytes)
        final byte[] keyBytes = jwtSecret.trim().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("JWT secret key must be at least 64 bytes for HS512");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }
    
    private SecretKey getSigningKey() {
        return this.secretKey;
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
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    public Long getUserIdFromToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        // Handle potential class cast by converting Number to Long explicitly
        final Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        return null;
    }
    
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid JWT token
            return false;
        }
    }
    
    public boolean isTokenExpired(final String token) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Treat token as expired if parsing fails
            return true;
        }
    }
}