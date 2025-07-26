package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for login requests containing user credentials.
 */
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * Default no-args constructor.
     */
    public LoginRequest() {
    }

    /**
     * Constructs a LoginRequest with the specified email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     */
    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the email address.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the email to set
     */
    public void setEmail(final String email) {
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
    public void setPassword(final String password) {
        this.password = password;
    }
}