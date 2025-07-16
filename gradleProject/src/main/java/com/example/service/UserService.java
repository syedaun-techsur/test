package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection for better testability and immutability
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @Transactional
    public User createUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the id of the user
     * @return the found user
     * @throws UserNotFoundException if user not found
     */
    public User getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    /**
     * Custom exception for user not found scenarios.
     */
    public static class UserNotFoundException extends NoSuchElementException {
        public UserNotFoundException(final String message) {
            super(message);
        }
    }
}