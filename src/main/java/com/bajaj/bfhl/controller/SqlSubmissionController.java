package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.service.SqlSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sql")
public class SqlSubmissionController {

    private final SqlSubmissionService sqlSubmissionService;

    @Autowired
    public SqlSubmissionController(SqlSubmissionService sqlSubmissionService) {
        this.sqlSubmissionService = sqlSubmissionService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitSqlQuery(
            @RequestHeader(value = "Authorization", required = false) String authToken) {
        
        // If token is provided in the header, use it
        if (authToken != null && !authToken.isEmpty()) {
            sqlSubmissionService.setAccessToken(authToken);
        }
        
        String response = sqlSubmissionService.submitSqlQuery();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/submit-with-token")
    public ResponseEntity<String> submitSqlQueryWithToken(
            @RequestHeader(value = "Authorization", required = true) String authToken) {
        
        String response = sqlSubmissionService.submitSqlQuery(authToken);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/generate-webhook")
    public ResponseEntity<String> generateWebhook() {
        String response = sqlSubmissionService.generateWebhook();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/query")
    public ResponseEntity<Map<String, String>> getSqlQuery() {
        Map<String, String> response = new HashMap<>();
        response.put("finalQuery", sqlSubmissionService.getSqlQuery());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("hasAccessToken", sqlSubmissionService.getAccessToken() != null && !sqlSubmissionService.getAccessToken().isEmpty());
        status.put("webhookUrl", "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA");
        return ResponseEntity.ok(status);
    }
} 