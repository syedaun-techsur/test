package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for greeting endpoints.
 * Handles requests related to hello messages.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {

    /**
     * Handles GET requests for "/hello" endpoint.
     * Returns a simple JSON message.
     *
     * @return a map with the greeting message
     */
    @GetMapping(value = "/hello", produces = "application/json")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from backend!");
    }
}