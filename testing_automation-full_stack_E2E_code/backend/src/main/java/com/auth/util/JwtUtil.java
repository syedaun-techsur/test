package com.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final int MIN_SECRET_KEY_LENGTH_BYTES = 64; // Recommended length for HS512

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private long jwtExpiration;

    /**
     * Returns the signing key used for JWT signing and validation.
     * Ensures the key meets minimum length requirement for HS512 algorithm.
     *
     * @return SecretKey for signing JWT
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < MIN_SECRET_KEY_LENGTH_BYTES) {
            throw new IllegalArgumentException("The JWT secret key must be at least " + MIN_SECRET_KEY_LENGTH_BYTES + " bytes for HS512 algorithm");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token with the given email as subject and userId as claim.
     *
     * @param email  user's email
     * @param userId user's ID
     * @return generated JWT token string
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
     * Extracts email (subject) from the JWT token.
     *
     * @param token JWT token
     * @return email from token
     */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts userId claim from the JWT token.
     *
     * @param token JWT token
     * @return userId from token or null if not present
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object userIdClaim = claims.get("userId");
        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue();
        }
        return null;
    }

    /**
     * Validates the JWT token's signature and format.
     *
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // log error for debug if a logging framework is available
            // e.g. log.error("Invalid JWT token", e);
            return false;
        }
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token JWT token
     * @return true if expired or invalid, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // log error if logging enabled
            return true;
        }
    }

    /**
     * Parses claims from the JWT token.
     *
     * @param token JWT token
     * @return Claims contained in the token
     * @throws JwtException if token is invalid
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}