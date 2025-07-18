package com.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long jwtExpiration;

    private byte[] getSecretKeyBytes() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT secret cannot be null or blank");
        }
        return jwtSecret.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] getSigningKey() {
        return getSecretKeyBytes();
    }

    /**
     * Generates a JWT token containing email as subject and userId as claim.
     *
     * @param email  The email to set as subject
     * @param userId The userId to add as a claim
     * @return The generated JWT token string
     */
    public String generateToken(final String email, final Long userId) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Retrieves email (subject) from the JWT token.
     *
     * @param token JWT token string
     * @return email contained in token
     */
    public String getEmailFromToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Retrieves userId from the JWT token claims.
     *
     * @param token JWT token string
     * @return userId contained in token or null if not present or invalid
     */
    public Long getUserIdFromToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        return null;
    }

    /**
     * Validates the JWT token signature and expiration.
     *
     * @param token JWT token string
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(getSigningKey()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log or handle token validation exception here if needed
            return false;
        }
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token JWT token string
     * @return true if token is expired or invalid, false otherwise
     */
    public boolean isTokenExpired(final String token) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(getSigningKey()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}