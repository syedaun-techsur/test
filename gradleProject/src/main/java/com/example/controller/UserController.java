package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieve all users.
     * @return list of all users
     */
    @GetMapping
    public List<User> getAllUsers() {
        logger.debug("Fetching all users");
        return userService.getAllUsers();
    }

    /**
     * Create a new user.
     * @param user the user to create
     * @return created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        logger.debug("Creating user: {}", user);
        return userService.createUser(user);
    }

    /**
     * Get a user by ID.
     * @param id user ID
     * @return user with given ID
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        logger.debug("Fetching user with id {}", id);
        return userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}