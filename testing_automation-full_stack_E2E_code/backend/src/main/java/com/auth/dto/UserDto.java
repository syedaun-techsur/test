package com.auth.dto;

import com.auth.entity.User;
import java.util.Objects;

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
     * Parameterized constructor.
     * 
     * @param id User ID
     * @param email User email
     * @param firstName User first name
     * @param lastName User last name
     */
    public UserDto(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructor to create UserDto from User entity.
     * 
     * @param user User entity
     */
    public UserDto(User user) {
        if (user != null) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
        }
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
        if (o == null || getClass() != o.getClass()) return false;

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