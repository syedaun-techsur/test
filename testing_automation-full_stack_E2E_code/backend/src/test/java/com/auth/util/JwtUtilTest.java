package com.auth.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String email;
    private Long userId;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 1000L); // 1 second for expiration test
        email = "test@example.com";
        userId = 1L;
    }

    @Test
    void generateToken_ShouldReturnNonEmptyToken() {
        // Act
        String token = jwtUtil.generateToken(email, userId);

        // Assert
        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void getEmailFromToken_ShouldReturnCorrectEmail() {
        // Arrange
        String token = jwtUtil.generateToken(email, userId);

        // Act
        String extractedEmail = jwtUtil.getEmailFromToken(token);

        // Assert
        assertEquals(email, extractedEmail, "Extracted email should match the original");
    }

    @Test
    void getUserIdFromToken_ShouldReturnCorrectUserId() {
        // Arrange
        String token = jwtUtil.generateToken(email, userId);

        // Act
        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        // Assert
        assertEquals(userId, extractedUserId, "Extracted user ID should match the original");
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = jwtUtil.generateToken(email, userId);

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid, "Invalid token should not be valid");
    }

    @Test
    void isTokenExpired_WithValidToken_ShouldReturnFalse() {
        // Arrange
        String token = jwtUtil.generateToken(email, userId);

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertFalse(isExpired, "Token just generated should not be expired");
    }

    @Test
    void isTokenExpired_WithExpiredToken_ShouldReturnTrue() throws InterruptedException {
        // Arrange
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 500L); // Set expiration to half second
        String token = jwtUtil.generateToken(email, userId);

        // Sleep to ensure token expires
        Thread.sleep(600);

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertTrue(isExpired, "Token should be expired");
    }
}