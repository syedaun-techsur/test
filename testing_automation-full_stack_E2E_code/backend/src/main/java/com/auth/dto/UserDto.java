package com.auth.dto;

/**
 * Data Transfer Object for User entity.
 * Immutable object representing a user.
 */
public class UserDto {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;

    /**
     * Constructs a UserDto with all required fields.
     *
     * @param id        the unique identifier of the user, must not be null
     * @param email     the email of the user, must not be null
     * @param firstName the first name of the user, must not be null
     * @param lastName  the last name of the user, must not be null
     */
    public UserDto(Long id, String email, String firstName, String lastName) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("email must not be null");
        }
        if (firstName == null) {
            throw new IllegalArgumentException("firstName must not be null");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("lastName must not be null");
        }

        this.id = id;
        this.email = email;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        if (!id.equals(userDto.id)) return false;
        if (!email.equals(userDto.email)) return false;
        if (!firstName.equals(userDto.firstName)) return false;
        return lastName.equals(userDto.lastName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
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