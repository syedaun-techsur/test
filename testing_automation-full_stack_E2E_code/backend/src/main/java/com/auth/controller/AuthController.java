package com.auth.controller;

import com.auth.dto.ErrorResponse;
import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles user login.
     * Validates input and returns JWT token or validation errors.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        }

        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Retrieves the current authenticated user based on the JWT token.
     */
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.length() < 7 || !authorizationHeader.startsWith("Bearer ")) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid or missing Authorization header");
        }

        try {
            String jwtToken = authorizationHeader.substring(7);

            if (!jwtUtil.validateToken(jwtToken)) {
                return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid token");
            }

            String email = jwtUtil.getEmailFromToken(jwtToken);
            UserDto user = authService.getUserByEmail(email);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    /**
     * Logs out the user.
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        // For real applications, token invalidation or blacklist logic should be here
        Map<String, String> response = Collections.singletonMap("message", "Logout successful");
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, List<String> errors) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, errors);
        return new ResponseEntity<>(errorResponse, status);
    }
}