package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object representing a login request.
 * Holds user email and password with validation constraints.
 */
public class LoginRequest {
    
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    /**
     * Default no-argument constructor.
     */
    public LoginRequest() {}
    
    /**
     * Parameterized constructor to create LoginRequest with email and password.
     *
     * @param email the email address of the user
     * @param password the password of the user
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    /**
     * Returns the email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}