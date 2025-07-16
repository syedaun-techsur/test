package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Controller that provides a simple hello endpoint.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {

    /**
     * GET endpoint that returns a simple greeting message.
     *
     * @return a map containing the message string
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from backend!");
    }
}