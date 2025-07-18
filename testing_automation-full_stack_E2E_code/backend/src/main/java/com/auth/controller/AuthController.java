package com.auth.controller;

import com.auth.dto.ErrorResponse;
import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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

    /**
     * Handles user login requests.
     *
     * @param loginRequest the login credentials
     * @param bindingResult the validation result
     * @return ResponseEntity with login response or validation errors
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(400, "Validation failed",
                    errors == null ? Collections.emptyList() : errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(401, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Retrieves the current authenticated user information using the JWT token.
     *
     * @param authorizationHeader the Authorization header containing the Bearer token
     * @return ResponseEntity with user information or error response if token is invalid
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.length() <= 7) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Authorization header missing or malformed");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            String jwtToken = authorizationHeader.substring(7);

            if (!jwtUtil.validateToken(jwtToken)) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            String email = jwtUtil.getEmailFromToken(jwtToken);
            UserDto user = authService.getUserByEmail(email);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint for user logout.
     * Note: This implementation returns a success message as no token invalidation is done.
     *
     * @return ResponseEntity with logout success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return a success message
        return ResponseEntity.ok().body("{\"message\": \"Logout successful\"}");
    }
}