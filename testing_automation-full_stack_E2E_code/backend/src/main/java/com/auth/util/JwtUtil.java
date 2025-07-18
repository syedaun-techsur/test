package com.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token containing email as subject and userId as a claim.
     *
     * @param email  the email to set as subject
     * @param userId the user ID to include as claim
     * @return the generated JWT token string
     */
    public String generateToken(String email, Long userId) {
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

    /**
     * Extracts the subject (email) from the JWT token.
     *
     * @param token the JWT token string
     * @return the email contained in the token subject
     */
    public String getEmailFromToken(String token) {
        final Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * Extracts the userId claim from the JWT token.
     *
     * @param token the JWT token string
     * @return the user ID contained in the token claims
     */
    public Long getUserIdFromToken(String token) {
        final Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Validates the JWT token.
     *
     * @param token the JWT token string
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
            logger.log(Level.WARNING, "Invalid JWT token: {0}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token string
     * @return true if expired or invalid, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            final Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "Unable to parse JWT token to check expiration: {0}", e.getMessage());
            return true;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}