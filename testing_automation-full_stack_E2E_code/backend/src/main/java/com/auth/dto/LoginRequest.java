package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for login request containing user email and password.
 */
public class LoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    /**
     * Default constructor.
     */
    public LoginRequest() {}

    /**
     * Constructor with all fields.
     * 
     * @param email user's email
     * @param password user's password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    /**
     * Gets the email.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email.
     * 
     * @param email user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the password.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     * 
     * @param password user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginRequest that = (LoginRequest) o;

        if (!email.equals(that.email)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}