package com.project.main.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.main.entity.UserPrompt;
import com.project.main.service.UserPromptService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private UserPromptService userPromptService;

    @PostMapping
    public ResponseEntity<String> receiveUserPrompt(@RequestBody String data) {
        System.out.println("Received data: " + data);

        ObjectMapper objectMapper = new ObjectMapper();
        UserPrompt userPrompt;
        
        try {
            userPrompt = objectMapper.readValue(data, UserPrompt.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to parse UserPrompt data");
        }
        
        userPromptService.saveUserPrompt(userPrompt); // saving user prompt using service
        
        return ResponseEntity.ok("UserPrompt saved successfully");
    }
}