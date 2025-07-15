package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Main class to bootstrap the Spring Boot backend application.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /**
     * REST controller exposing API endpoints under /api.
     * Enables CORS for frontend running on http://localhost:3000.
     */
    @RestController
    @RequestMapping("/api")
    @CrossOrigin(origins = "http://localhost:3000")
    public static class HelloController {

        /**
         * GET endpoint /hello returning a simple JSON message.
         *
         * @return map with a greeting message
         */
        @GetMapping("/hello")
        public Map<String, String> hello() {
            return Map.of("message", "Hello from backend!");
        }
    }
}