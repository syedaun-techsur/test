package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing simple greeting endpoints.
 */
@RestController
public class HelloController {

    /**
     * Endpoint to return a personalized greeting.
     *
     * @param name the name to greet, defaults to "World" if not provided
     * @return a greeting string
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    /**
     * Home endpoint returning a welcome message.
     *
     * @return welcome string
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to Simple Spring Boot Application (Maven)!";
    }
}