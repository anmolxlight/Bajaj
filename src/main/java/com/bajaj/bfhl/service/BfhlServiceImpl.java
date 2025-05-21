package com.bajaj.bfhl.service;

import com.bajaj.bfhl.model.BfhlRequest;
import com.bajaj.bfhl.model.JsonResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BfhlServiceImpl implements BfhlService {

    @Override
    public JsonResponse processData(BfhlRequest request) {
        List<String> data = request.getData();
        List<String> numbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        
        // Process data to separate numbers and alphabets
        if (data != null) {
            for (String item : data) {
                if (item.matches("[0-9]+")) {
                    numbers.add(item);
                } else if (item.matches("[a-zA-Z]")) {
                    alphabets.add(item);
                }
            }
        }
        
        // Create response with required fields
        return new JsonResponse(
                true,
                "anmol_sharma_16052001",
                "anmol0516.be20@chitkara.edu.in",
                "2011981051",
                numbers,
                alphabets
        );
    }
} 