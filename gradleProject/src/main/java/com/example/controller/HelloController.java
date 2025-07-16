package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles simple greeting requests.
 */
@RestController
public class HelloController {

    /**
     * Endpoint to return a greeting message.
     * 
     * @param name the name to greet, defaults to "World" if not provided.
     * @return a greeting string.
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") final String name) {
        return String.format("Hello %s!", name);
    }

    /**
     * Endpoint to return a welcome message for the root path.
     * 
     * @return a welcome string.
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to Simple Spring Boot Application!";
    }
}