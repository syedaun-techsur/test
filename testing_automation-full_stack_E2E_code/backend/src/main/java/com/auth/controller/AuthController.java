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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
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
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(401, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX) || authorizationHeader.length() <= BEARER_PREFIX.length()) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Missing or invalid Authorization header");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        try {
            String jwtToken = authorizationHeader.substring(BEARER_PREFIX.length());

            if (!jwtUtil.validateToken(jwtToken)) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            String email = jwtUtil.getEmailFromToken(jwtToken);
            UserDto user = authService.getUserByEmail(email);

            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return a success message
        return ResponseEntity.ok().body("{\"message\": \"Logout successful\"}");
    }
}