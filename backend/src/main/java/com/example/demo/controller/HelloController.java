package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller providing hello message endpoint for frontend integration.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public final class HelloController {

    /**
     * GET endpoint to return a welcome message.
     *
     * @return a map with a single "message" entry.
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from backend!");
    }
}