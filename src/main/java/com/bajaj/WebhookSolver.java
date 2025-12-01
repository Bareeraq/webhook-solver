package com.bajaj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Component
public class WebhookSolver {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    
    // REPLACE THESE WITH YOUR DETAILS:
    private static final String NAME = "Bareera Qureishi";
    private static final String REG_NO = "22BCE11440";
    private static final String EMAIL = "bareera.qureishi7@gmail.com";
    
    @EventListener(ApplicationReadyEvent.class)
    public void solveProblem() {
        try {
            System.out.println("Starting Bajaj Test Solution...");
            
            // Step 1: Generate webhook
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", NAME);
            requestBody.put("regNo", REG_NO);
            requestBody.put("email", EMAIL);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(
                GENERATE_WEBHOOK_URL, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String webhookUrl = (String) responseBody.get("webhook");
                String accessToken = (String) responseBody.get("accessToken");
                
                System.out.println("Webhook URL: " + webhookUrl);
                
                // Step 2: Determine question type
                String lastTwoDigits = REG_NO.substring(REG_NO.length() - 2);
                int lastDigit = Integer.parseInt(lastTwoDigits.substring(lastTwoDigits.length() - 1));
                String questionType = (lastDigit % 2 == 0) ? "EVEN" : "ODD";
                
                System.out.println("Question Type: " + questionType);
                
                // Step 3: Solve SQL problem
                String sqlQuery = solveSqlProblem(questionType);
                System.out.println("SQL Query: " + sqlQuery);
                
                // Step 4: Submit solution
                submitSolution(webhookUrl, accessToken, sqlQuery);
                
            } else {
                System.err.println("Failed to get webhook");
            }
            
        } catch (Exception e) {
            System.err.println(Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String solveSqlProblem(String questionType) {
        // TODO: Check your Google Drive link and implement SQL
        
        if ("ODD".equals(questionType)) {
            // Question 1 SQL (EXAMPLE - replace with actual)
            return "SELECT * FROM employees WHERE salary > 50000;";
        } else {
            // Question 2 SQL (EXAMPLE - replace with actual)
            return "SELECT department, AVG(salary) FROM employees GROUP BY department;";
        }
    }
    
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("finalQuery", sqlQuery);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                webhookUrl,
                request,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Solution submitted successfully!");
                System.out.println("Response: " + response.getBody());
            } else {
                System.err.println("Submission failed: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("Submit error: " + e.getMessage());
        }
    }
}