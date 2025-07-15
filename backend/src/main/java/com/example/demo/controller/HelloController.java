package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public final class HelloController {

    /**
     * Handles GET requests to /api/hello and returns a greeting message.
     *
     * @return HelloResponse containing the greeting message.
     */
    @GetMapping("/hello")
    public HelloResponse hello() {
        return new HelloResponse("Hello from backend!");
    }

    /**
     * Simple DTO for hello response.
     */
    public static class HelloResponse {
        private final String message;

        public HelloResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}