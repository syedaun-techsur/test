package com.auth.dto;

public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    
    // Constructors
    public UserDto() {}

    public UserDto(Long id, String email, String firstName, String lastName) {
        this.id = id;
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
    }
    
    // Getters and Setters

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty() && email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        if (lastName != null && !lastName.trim().isEmpty()) {
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}