package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.model.BfhlRequest;
import com.bajaj.bfhl.model.JsonResponse;
import com.bajaj.bfhl.service.BfhlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private final BfhlService bfhlService;

    @Autowired
    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    @PostMapping
    public ResponseEntity<JsonResponse> processData(@RequestBody BfhlRequest request) {
        JsonResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Object> getOperationCode() {
        return ResponseEntity.ok().body("{\"operation_code\":1}");
    }
} 