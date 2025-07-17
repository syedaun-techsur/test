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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final LoginRequest loginRequest, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            final ErrorResponse errorResponse = new ErrorResponse(400, "Validation failed", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            final LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            final ErrorResponse errorResponse = new ErrorResponse(401, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) final String token) {
        try {
            if (token == null || !token.startsWith("Bearer ") || token.length() <= 7) {
                final ErrorResponse errorResponse = new ErrorResponse(401, "Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            final String jwtToken = token.substring(7);

            if (!jwtUtil.validateToken(jwtToken)) {
                final ErrorResponse errorResponse = new ErrorResponse(401, "Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            final String email = jwtUtil.getEmailFromToken(jwtToken);
            final UserDto user = authService.getUserByEmail(email);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            final ErrorResponse errorResponse = new ErrorResponse(401, "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return a success message
        return ResponseEntity.ok(Collections.singletonMap("message", "Logout successful"));
    }
}