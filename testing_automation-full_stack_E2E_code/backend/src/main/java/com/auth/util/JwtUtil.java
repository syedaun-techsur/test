package com.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * Utility class for generating, validating, and parsing JSON Web Tokens (JWT).
 */
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private long jwtExpiration;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT token containing the email as subject and userId as claim.
     * Token is valid for the configured expiration time.
     *
     * @param email  the user's email
     * @param userId the user's ID
     * @return the JWT token string
     */
    public String generateToken(final String email, final Long userId) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), signatureAlgorithm)
                .compact();
    }

    /**
     * Extract email (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return email extracted from token
     */
    public String getEmailFromToken(final String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Extract userId claim from JWT token.
     *
     * @param token the JWT token
     * @return userId or null if not present or invalid
     */
    public Long getUserIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        } else if (userIdObj instanceof String) {
            try {
                return Long.parseLong((String) userIdObj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Validates the JWT token signature and expiration.
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Could add logging here for failed validation, e.g.:
            // log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT token
     * @return true if expired or invalid, false otherwise
     */
    public boolean isTokenExpired(final String token) {
        try {
            final Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            // Consider token expired if exception occurs
            return true;
        }
    }

    /**
     * Extract claims from the JWT token.
     *
     * @param token the JWT token
     * @return Claims object or null if token invalid
     */
    private Claims getClaimsFromToken(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // Could add logging here for debugging purposes
            return null;
        }
    }
}