package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {

    @Test
    public void contextLoads() {
        // Basic check to ensure the application context loads without issues
        assertThat(true).isTrue();
    }

}