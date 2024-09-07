package com.project.main.controller;

import com.project.main.entity.UserPrompt;
import com.project.main.service.UserPromptService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/user-prompts")
public class UserPromptController {
    private final UserPromptService userPromptService;
    
    public UserPromptController(UserPromptService userPromptService) {
        this.userPromptService = userPromptService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserPrompt>> getAllUserPrompts() {
        return ResponseEntity.ok().body(this.userPromptService.getAllUserPrompts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPrompt> getUserPromptById(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.userPromptService.getUserPromptById(id));
    }

    @PostMapping("/")
    public ResponseEntity<UserPrompt> saveUserPrompt(@RequestBody UserPrompt userPrompt)
    {
        return ResponseEntity.ok().body(this.userPromptService.saveUserPrompt(userPrompt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserPromptById(@PathVariable Integer id)
    {
        this.userPromptService.deleteUserPromptById(id);
        return ResponseEntity.ok().body("Deleted user prompt successfully");
    }
}