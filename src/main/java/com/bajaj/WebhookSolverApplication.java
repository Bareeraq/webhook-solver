package com.bajaj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Remove the RestTemplate bean from here
public class WebhookSolverApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebhookSolverApplication.class, args);
    }
}