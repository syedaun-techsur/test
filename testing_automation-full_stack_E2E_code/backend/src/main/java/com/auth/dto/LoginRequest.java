package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for login requests containing user email and password.
 */
public final class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * Default constructor.
     */
    public LoginRequest() {
    }

    /**
     * Parameterized constructor.
     * 
     * @param email    User email
     * @param password User password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the user email.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user email.
     * 
     * @param email User email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user password.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user password.
     * 
     * @param password User password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the LoginRequest.
     */
    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}