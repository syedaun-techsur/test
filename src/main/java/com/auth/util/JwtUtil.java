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
    
    @Value("${jwt.secret:MyVeryStrongDefaultJwtSecretKeyForTokenGeneration!}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long jwtExpiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate JWT token with email as subject and userId as claim.
     *
     * @param email the user's email
     * @param userId the user's ID
     * @return generated JWT token string
     */
    public String generateToken(String email, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (jwtExpiration != null ? jwtExpiration : 86400000L));
        
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract email (subject) from the given JWT token.
     *
     * @param token JWT token string
     * @return email extracted from token or null if invalid
     */
    public String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * Extract userId from the given JWT token.
     *
     * @param token JWT token string
     * @return userId extracted from token or null if invalid or absent
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            }
        }
        return null;
    }

    /**
     * Validate the given JWT token.
     *
     * @param token JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log exception for debug (replace with logger in real app)
            System.err.println("JWT validation error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if the JWT token is expired.
     *
     * @param token JWT token string
     * @return true if expired or invalid, false if valid and not expired
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        return expiration == null || expiration.before(new Date());
    }

    /**
     * Parse JWT token and return claims, or null if token is invalid.
     *
     * @param token JWT token string
     * @return Claims object or null if invalid token
     */
    private Claims parseToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // Log exception for debug (replace with logger in real app)
            System.err.println("Failed to parse JWT token: " + e.getMessage());
            return null;
        }
    }
}