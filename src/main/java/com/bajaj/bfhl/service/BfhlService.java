package com.bajaj.bfhl.service;

import com.bajaj.bfhl.model.BfhlRequest;
import com.bajaj.bfhl.model.JsonResponse;

public interface BfhlService {
    JsonResponse processData(BfhlRequest request);
} 