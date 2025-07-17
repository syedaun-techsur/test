package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO representing login request data with email and password.
 */
public class LoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private final String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private final String password;
    
    /**
     * Constructs a LoginRequest with the provided email and password.
     * 
     * @param email    the user's email address
     * @param password the user's password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
               "email='" + email + '\'' +
               ", password='[PROTECTED]'" +
               '}';
    }
}