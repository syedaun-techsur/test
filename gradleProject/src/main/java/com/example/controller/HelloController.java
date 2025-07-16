package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that handles simple greeting endpoints.
 */
@RestController
public class HelloController {

    /**
     * Returns a greeting message.
     *
     * @param name the name to greet, defaults to "World"
     * @return a greeting string
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    /**
     * Returns a welcome message for the home endpoint.
     *
     * @return a welcome string
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to Simple Spring Boot Application!";
    }
}