package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String email = "test@example.com";
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
    }

    private String generateToken() {
        return jwtUtil.generateToken(email, userId);
    }

    @Test
    @DisplayName("Test: Generate Token - Should Generate Non-null, Non-empty Token")
    void testGenerateToken() {
        String token = generateToken();

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    @DisplayName("Test: Extract Email from Token")
    void testGetEmailFromToken() {
        String token = generateToken();

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail, "Extracted email should match the original email");
    }

    @Test
    @DisplayName("Test: Extract UserId from Token")
    void testGetUserIdFromToken() {
        String token = generateToken();

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId, "Extracted userId should match the original userId");
    }

    @Test
    @DisplayName("Test: Validate Token - Valid Token Should Pass")
    void testValidateTokenValid() {
        String token = generateToken();

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Token should be valid");
    }

    @Test
    @DisplayName("Test: Validate Token - Invalid Token Should Fail")
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should be reported as invalid");
    }

    @Test
    @DisplayName("Test: Check Token Expiry - Token Should Not be Expired")
    void testIsTokenExpiredFalse() {
        String token = generateToken();

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Token should not be expired");
    }

    @Test
    @DisplayName("Test: Check Token Expiry - Invalid Token Should be Treated as Expired")
    void testIsTokenExpiredTrue() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired, "Invalid token should be treated as expired");
    }
}