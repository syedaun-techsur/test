package com.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object for login requests.
 * Contains user email and password with validation constraints.
 */
public final class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private final String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private final String password;

    /**
     * Constructs a new LoginRequest with the specified email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the email of the user.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password of the user.
     *
     * @return password string
     */
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