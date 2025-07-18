package com.auth.dto;

import java.beans.ConstructorProperties;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    public UserDto() {
    }

    /**
     * Parameterized constructor.
     *
     * @param id        the user ID
     * @param email     the user's email
     * @param firstName the user's first name
     * @param lastName  the user's last name
     */
    @ConstructorProperties({"id", "email", "firstName", "lastName"})
    public UserDto(@Nullable Long id, @Nullable String email, @Nullable String firstName, @Nullable String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
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
    public boolean equals(@Nullable Object o) {
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