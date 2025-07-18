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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Allow cross-origin requests from frontend
public class AuthController {

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
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(400, "Validation failed", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Authentication failed: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || token.length() < 7 || !token.startsWith("Bearer ")) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Authorization header missing or invalid");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            // Remove "Bearer " prefix safely
            String jwtToken = token.substring(7);

            if (!jwtUtil.validateToken(jwtToken)) {
                ErrorResponse errorResponse = new ErrorResponse(401, "Invalid or expired token");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            String email = jwtUtil.getEmailFromToken(jwtToken);
            UserDto user = authService.getUserByEmail(email);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(401, "Invalid token or unable to retrieve user");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return a success message
        return ResponseEntity.ok("{\"message\": \"Logout successful\"}");
    }
}