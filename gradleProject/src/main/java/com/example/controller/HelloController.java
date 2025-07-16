package com.example.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling basic greeting endpoints.
 */
@RestController
@RequestMapping
@Validated
public class HelloController {

    /**
     * Endpoint to greet the provided name or default to "World" if none given.
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
     * @return a welcome string message
     */
    @GetMapping("/")
    public String home() {
        return "Welcome to Simple Spring Boot Application!";
    }
}