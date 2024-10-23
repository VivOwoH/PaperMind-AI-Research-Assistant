package com.project.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.main.service.GeminiService;

@RestController
@RequestMapping("/api/generate-summary")
@CrossOrigin(origins = "http://localhost:3000")
public class AISummaryController {

	@Autowired
	GeminiService geminiService;

	@Value("${apiKey}")
    private String apiKey;

	@PostMapping
	public ResponseEntity<String> generateAISummary(@RequestBody String content) {
		// gemini (1): AI summary of abstract
        ObjectMapper objectMapper = new ObjectMapper();

		String aiSummary = String.format(
				"Paraphraze this paragraph that is abstract of an academic paper: '%s', aiming for general audience, do not use technical jargon or give simple explanation when technical terms are used. The output should be one cohesive paragraph so no list or dot points. If you receive empty abstract or cannot summarize for any reason, return empty string.",
				content);

		String responseBody = geminiService.callApi(aiSummary, apiKey);
        String response = null;
        
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody); // formatting
            response = jsonNode.path("candidates").get(0)
					.path("content").path("parts").get(0)
					.path("text").asText()
					.replaceAll("\\*|\\n", " ").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        // System.out.println(response);

		if (response != null) {
            return ResponseEntity.ok(response); // final output: 1 paragraph of rephrased abstract
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No gemini response (1) received.");
        }
	}
}

