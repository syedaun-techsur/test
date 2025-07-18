package com.auth.controller;

import com.auth.dto.ErrorResponse;
import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(400, "Validation failed", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(401, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer ")) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Authorization header missing or invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            String jwtToken = authorizationHeader.substring(7).trim();

            if (!jwtUtil.validateToken(jwtToken)) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            String email = jwtUtil.getEmailFromToken(jwtToken);
            if (email == null || email.isBlank()) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token payload");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            UserDto user = authService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (JwtException | IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might want to blacklist the token or clear security context
        // For now, just return a success message as JSON
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}