package com.auth.dto;

import java.util.Objects;

/**
 * Data Transfer Object for User.
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
     * Parameterized constructor.
     *
     * @param id        the user ID
     * @param email     the user's email
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) &&
               Objects.equals(email, userDto.email) &&
               Objects.equals(firstName, userDto.firstName) &&
               Objects.equals(lastName, userDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
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