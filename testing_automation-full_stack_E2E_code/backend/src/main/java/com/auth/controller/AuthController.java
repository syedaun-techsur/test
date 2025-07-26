package com.auth.controller;

import com.auth.dto.ErrorResponse;
import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {

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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String jwtToken = authorizationHeader.substring(BEARER_PREFIX.length());

        if (!jwtUtil.validateToken(jwtToken)) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        try {
            String email = jwtUtil.getEmailFromToken(jwtToken);
            UserDto user = authService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return a success message as JSON map
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, List<String> errors) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, errors);
        return new ResponseEntity<>(errorResponse, status);
    }
}