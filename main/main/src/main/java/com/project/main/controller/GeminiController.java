package com.project.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.main.service.GeminiService;

@RestController
public class GeminiController {
	
	@Autowired
	GeminiService geminiService;
	
	@GetMapping("/prompt")
	public ResponseEntity<String> getResponse(String prompt, String geminiKey) {
		String responseBody = geminiService.callApi(prompt,geminiKey);
		return ResponseEntity.ok().body(responseBody);
	}
}