package com.project.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.main.entity.UserPrompt;
import com.project.main.service.GeminiService;
import com.project.main.service.SemanticService;
import com.project.main.service.UserPromptService;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "http://localhost:3000")
public class APIController {

	@Autowired
	GeminiService geminiService;

	private final UserPromptService userPromptService;
	private final SemanticService semanticService;

	public APIController(UserPromptService userPromptService, SemanticService semanticService) {
		this.userPromptService = userPromptService;
		this.semanticService = semanticService;
	}

	@PostMapping
	public ResponseEntity<String> receiveUserPrompt(@RequestBody UserPrompt userPrompt) {

		userPromptService.saveUserPrompt(userPrompt); // saving user prompt using service

		// gemini: tuned user_prompt for a list of extracted keywords
		/**
		 * E.g.
		 * extract key words and determine positive or negative sentiment for statement "Channel estimation with pilots is outdated";
		 * must structure response like this: "keywords:keyword1, keyword2 etc.; sentiment:positive/negative;"
		 */
		String formattedPrompt = String.format(
				"extract key words and determine positive or negative sentiment for statement '%s', only keep neural terms (non-sentimental terms) as keywords; must structure response like this: 'keywords:keyword1, keyword2 etc.; sentiment:positive/negative;'",
				userPrompt.getSearchPrompt());

		userPrompt.setSearchPrompt(formattedPrompt); // reset user prompt to tuned prompt
		String responseBody = geminiService.callApi(userPrompt, "AIzaSyDLmB0f1-lmo2-WH9Dif0fC32t0_Z9Hpuo"); // TODO: encrypt key
		
		// response formatting
		ObjectNode responseJSON = responseFormatting(responseBody).getBody();

		System.out.println(responseJSON);

		// semantic: use the list of extracted keywords to get a JSON list of papers
		String semanticQuery = responseJSON.get("keywords").asText()
						.replace(",", "")
						.replaceAll(";$", "").trim();
		String fetchedPapers = semanticService.paperRelevanceSearch(semanticQuery);
		
		// gemini: tuned_prompt for top 10 papers

		// output: JSON list of 10 papers, or input required for the graph back to react via request
		return ResponseEntity.ok(fetchedPapers);
	}

	public ResponseEntity<ObjectNode> responseFormatting(String responseBody) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			String text = jsonNode.path("candidates").get(0)
					.path("content").path("parts").get(0)
					.path("text").asText()
					.replaceAll("\\*|\\n", " ").trim();

			String keywords = text.substring(text.indexOf("keywords:") + 9, text.indexOf("sentiment:")).trim();
			String sentiment = text.substring(text.indexOf("sentiment:") + 10).trim();

			ObjectNode responseJson = objectMapper.createObjectNode();
			responseJson.put("keywords", keywords);
			responseJson.put("sentiment", sentiment);

			return ResponseEntity.ok(responseJson);
		} 
		catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}