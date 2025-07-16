package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates and saves a new user.
     *
     * @param user the user to create
     * @return the saved user entity
     */
    public User createUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user's ID
     * @return the found user
     * @throws UserNotFoundException if no user exists with the given ID
     */
    public User getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // Custom exception for clearer error handling
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(final String message) {
            super(message);
        }
    }
}