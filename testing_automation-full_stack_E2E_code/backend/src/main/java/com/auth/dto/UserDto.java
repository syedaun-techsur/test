package com.auth.dto;

/**
 * Data Transfer Object for User entity.
 */
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    
    /**
     * Default constructor.
     */
    public UserDto() {}

    /**
     * Parameterized constructor to create UserDto.
     *
     * @param id        the user ID
     * @param email     the user email
     * @param firstName the user's first name
     * @param lastName  the user's last name
     */
    public UserDto(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns a string representation of the UserDto.
     */
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