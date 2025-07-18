package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

   @BeforeEach
void setUp() {
    jwtUtil = new JwtUtil();
    ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
    ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
}

@Test
void generateToken_ShouldReturnValidToken() {
    // Arrange
    String email = "test@example.com";
    Long userId = 1L;

    // Act
    String token = jwtUtil.generateToken(email, userId);

    // Assert
    assertNotNull(token, "Generated token should not be null");
    assertFalse(token.isEmpty(), "Generated token should not be empty");
}

@Test
void getEmailFromToken_ShouldReturnCorrectEmail() {
    // Arrange
    String email = "test@example.com";
    Long userId = 1L;
    String token = jwtUtil.generateToken(email, userId);

    // Act & Assert
    assertEquals(email, jwtUtil.getEmailFromToken(token));
}

@Test
void getUserIdFromToken_ShouldReturnCorrectUserId() {
    // Arrange
    String email = "test@example.com";
    Long userId = 1L;
    String token = jwtUtil.generateToken(email, userId);

    // Act & Assert
    assertEquals(userId, jwtUtil.getUserIdFromToken(token));
}

@Test
void validateToken_ShouldReturnTrueForValidToken() {
    // Arrange
    String token = jwtUtil.generateToken("valid@example.com", 10L);

    // Act & Assert
    assertTrue(jwtUtil.validateToken(token));
}

@Test
void validateToken_ShouldReturnFalseForInvalidToken() {
    // Malformed token string should be invalid
    assertFalse(jwtUtil.validateToken("invalid.token.here"));
}

@Test
void isTokenExpired_ShouldReturnFalseForValidToken() {
    // Arrange
    String token = jwtUtil.generateToken("test@example.com", 1L);

    // Act & Assert
    assertFalse(jwtUtil.isTokenExpired(token));
}

@Test
void isTokenExpired_ShouldReturnTrueForMalformedToken() {
    // Malformed token considered expired for safety
    assertTrue(jwtUtil.isTokenExpired("invalid.token.here"));
}

@Test
void isTokenExpired_ShouldReturnTrueForExpiredToken() throws InterruptedException {
    // Arrange
    // Temporarily set expiration to 1 ms for testing expired token
    ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 1L);
    String token = jwtUtil.generateToken("expired@example.com", 2L);

    // Sleep to ensure token expires
    Thread.sleep(10);

    // Act & Assert
    assertTrue(jwtUtil.isTokenExpired(token));

    // Reset expiration to original for other tests
    ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L);
}
}