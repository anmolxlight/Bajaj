package com.bajaj.bfhl.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SqlSubmissionService {

    private static final String SQL_QUERY = "SELECT DISTINCT c.customer_id, c.name, c.email, c.mobile, COUNT(o.order_id) AS total_orders FROM customers c JOIN orders o ON c.customer_id = o.customer_id WHERE o.order_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) GROUP BY c.customer_id, c.name, c.email, c.mobile HAVING COUNT(o.order_id) > 5 ORDER BY total_orders DESC;";

    private final RestTemplate restTemplate;
    
    @Value("${bfhl.webhook.url:https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA}")
    private String webhookUrl;
    
    private String accessToken;

    public SqlSubmissionService() {
        this.restTemplate = new RestTemplate();
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // Log that the application is ready
        System.out.println("Application is ready. SQL Query is prepared for submission.");
        System.out.println("To submit the query, please use the /sql/submit endpoint with a valid access token.");
    }
    
    public String generateWebhook() {
        try {
            // Create empty request body
            Map<String, String> requestBody = new HashMap<>();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            
            // Send a POST request to generate webhook
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(webhookUrl, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();
            
            if (response != null && response.containsKey("accessToken")) {
                this.accessToken = (String) response.get("accessToken");
                return "Webhook generated successfully. Access token received.";
            } else {
                return "Failed to generate webhook: No access token in response";
            }
        } catch (Exception e) {
            return "Error generating webhook: " + e.getMessage();
        }
    }

    public String submitSqlQuery() {
        return submitSqlQuery(this.accessToken);
    }
    
    public String submitSqlQuery(String token) {
        if (token == null || token.isEmpty()) {
            return "Cannot submit SQL query: No access token provided.";
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("finalQuery", SQL_QUERY);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.postForObject(webhookUrl, entity, String.class);
        } catch (Exception e) {
            return "Error submitting SQL query: " + e.getMessage();
        }
    }
    
    public String getSqlQuery() {
        return SQL_QUERY;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
} 