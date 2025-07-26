package com.auth.dto;

/**
 * Data Transfer Object for User entity.
 * Contains basic user information.
 */
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    
    // Constructors
    public UserDto() {
    }
    
    public UserDto(final Long id, final String email, final String firstName, final String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(final String lastName) {
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;

        final UserDto userDto = (UserDto) o;

        if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
        if (email != null ? !email.equals(userDto.email) : userDto.email != null) return false;
        if (firstName != null ? !firstName.equals(userDto.firstName) : userDto.firstName != null) return false;
        return lastName != null ? lastName.equals(userDto.lastName) : userDto.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}