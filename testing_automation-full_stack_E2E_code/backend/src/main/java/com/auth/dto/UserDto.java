package com.auth.dto;

import java.util.Objects;

/**
 * Data Transfer Object for User entity.
 * Immutable class representing user details.
 */
public class UserDto {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;

    /**
     * Constructs an immutable UserDto.
     *
     * @param id        the user ID, must not be null
     * @param email     the user's email, must not be null
     * @param firstName the user's first name, nullable
     * @param lastName  the user's last name, nullable
     */
    public UserDto(Long id, String email, String firstName, String lastName) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
        return id.equals(userDto.id) &&
                email.equals(userDto.email) &&
                Objects.equals(firstName, userDto.firstName) &&
                Objects.equals(lastName, userDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }
}