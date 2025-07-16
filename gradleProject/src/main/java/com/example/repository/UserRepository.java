package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing User entities.
 * Extends JpaRepository to provide CRUD operations and custom finder methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a User entity by its email address.
     *
     * @param email the email address of the user
     * @return the User entity matching the given email, or null if none found
     */
    User findByEmail(String email);
}