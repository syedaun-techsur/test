package com.auth.dto;

import java.util.Objects;

/**
 * Data Transfer Object for User information.
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
     * @param id        User ID
     * @param email     User email
     * @param firstName User first name
     * @param lastName  User last name
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

    /**
     * Set email. If null or empty, sets email to null.
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = (email == null || email.trim().isEmpty()) ? null : email.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    /**
     * Set first name. Trimmed input or null if empty.
     * @param firstName user first name
     */
    public void setFirstName(String firstName) {
        this.firstName = (firstName == null || firstName.trim().isEmpty()) ? null : firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Set last name. Trimmed input or null if empty.
     * @param lastName user last name
     */
    public void setLastName(String lastName) {
        this.lastName = (lastName == null || lastName.trim().isEmpty()) ? null : lastName.trim();
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
}