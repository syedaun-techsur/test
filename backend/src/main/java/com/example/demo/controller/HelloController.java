package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Map.of;

/**
 * REST controller for handling hello requests.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {

    /**
     * Endpoint to return a simple greeting message.
     *
     * @return a map containing a greeting message
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return of("message", "Hello from backend!");
    }
}