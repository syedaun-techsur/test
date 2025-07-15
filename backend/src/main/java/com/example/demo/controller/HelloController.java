package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller to handle greeting endpoints.
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * Endpoint to return a greeting message.
     * 
     * @return a map containing the greeting message.
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from backend!");
    }
}