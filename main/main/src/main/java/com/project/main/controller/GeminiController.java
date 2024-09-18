package com.project.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.main.entity.UserPrompt;
import com.project.main.service.GeminiService;
import com.project.main.service.UserPromptService;

@RestController
@RequestMapping("/api/data")
public class GeminiController {
	
	@Autowired
	GeminiService geminiService;

	private final UserPromptService userPromptService;

    public GeminiController(UserPromptService userPromptService) {
        this.userPromptService = userPromptService;
    }

	@PostMapping
    public ResponseEntity<String> receiveUserPrompt(@RequestBody UserPrompt userPrompt) {
        System.out.println("Received data: " + userPrompt);
        
        userPromptService.saveUserPrompt(userPrompt); // saving user prompt using service
		String responseBody = geminiService.callApi(userPrompt, "AIzaSyDLmB0f1-lmo2-WH9Dif0fC32t0_Z9Hpuo");
        
		// gemini: tuned user_prompt for a list of extracted keywords

		// semantic: use the list of extracted keywords to get a JSON list of papers

		// gemini: tuned_prompt for top 10 papers

		// output: JSON list of 10 papers, or input required for the graph back to react via request

        return ResponseEntity.ok(responseBody);
    }
	
	@GetMapping("/prompt")
	public ResponseEntity<String> getResponse(UserPrompt prompt, String geminiKey) {
		String responseBody = geminiService.callApi(prompt,geminiKey);
		return ResponseEntity.ok().body(responseBody);
	}
}