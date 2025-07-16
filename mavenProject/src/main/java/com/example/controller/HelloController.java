package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle simple Hello and Home endpoints.
 */
@RestController
public class HelloController {

    /**
     * Endpoint to greet the user with the provided name or default to "World".
     *
     * @param name the name to greet, defaults to "World" if not provided
     * @return a greeting string
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    /**
     * Home endpoint providing a welcome message.
     *
     * @return welcome message string
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to Simple Spring Boot Application (Maven)!";
    }
}