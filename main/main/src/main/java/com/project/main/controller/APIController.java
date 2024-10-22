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

import org.json.JSONArray;

import com.project.main.entity.*;
import com.project.main.service.*;
import com.project.main.enums.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "http://localhost:3000")
public class APIController {

	@Autowired
	GeminiService geminiService;
	private final SemanticService semanticService;

	private final UserPromptService userPromptService;
	private final TokenResponseService tokenResponseService;
	private final AppResponseService appResponseService;

	@Autowired
	GraphService graphService;

	@Autowired
	AuthorService authorService;

	@Autowired
	ResearchPaperService researchPaperService;

	@Autowired
	CitationEdgeService citationEdgeService;

	@Autowired
	OpinionService opinionService;

	@Autowired
	OpinionRelatedPaperService opinionRelatedPaperService;

	@Autowired
	OpinionEdgeService opinionEdgeService;

	@Autowired
	OpinionEdgeIdService opinionEdgeIdService;

	public APIController(UserPromptService userPromptService, SemanticService semanticService, TokenResponseService tokenResponseService, AppResponseService appResponseService) {
		this.userPromptService = userPromptService;
		this.semanticService = semanticService;
		this.tokenResponseService = tokenResponseService;
		this.appResponseService = appResponseService;
	}

	@PostMapping
	public ResponseEntity<String> mainAPIflow(@RequestBody UserPrompt userPrompt) {

		ObjectMapper objectMapper = new ObjectMapper();
		
		userPromptService.saveUserPrompt(userPrompt); // saving user prompt using service

		// gemini (1): tuned user_prompt for a list of extracted keywords
		/**
		 * E.g.
		 * extract key words and determine positive or negative sentiment for statement "Channel estimation with pilots is outdated";
		 * must structure response like this: "keywords:keyword1, keyword2 etc.; sentiment:positive/negative;"
		 */
		String formattedPrompt = String.format(
				"extract key words and determine positive or negative sentiment for statement '%s', only keep neural terms (non-sentimental terms) as keywords; must structure response like this: 'keywords:keyword1, keyword2 etc.; sentiment:positive/negative;'",
				userPrompt.getSearchPrompt());

		// userPrompt.setSearchPrompt(formattedPrompt); // reset user prompt to tuned prompt
		String responseBody = geminiService.callApi(formattedPrompt, "AIzaSyDLmB0f1-lmo2-WH9Dif0fC32t0_Z9Hpuo"); // TODO: encrypt key
		
		ObjectNode responseJSON = responseFormatting(responseBody).getBody(); // response formatting

		if (responseJSON == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No gemini response (1) received.");

		// create token response
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setUserPrompt(userPrompt);
		tokenResponse.setProcessedPrompt(responseJSON.toString());

		// semantic (1): use the list of extracted keywords to get a JSON list of papers
		String semanticQuery = responseJSON.get("keywords").asText()
						.replace(",", "")
						.replaceAll(";$", "").trim();

		/**
		 * Note: Fetched Papers are limited by a certain number in descending order from most cited 
		 */
		JsonNode fetchedPapers = semanticService.paperRelevanceSearch(semanticQuery).getBody();

		if (fetchedPapers == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No semantic response (1) received.");

		// save all research papers
		for (JsonNode paperNode : fetchedPapers) {
			String title = paperNode.get("title").asText();		
			String paperId = paperNode.get("paperId").asText();			
			LocalDate publishedDate = null; // Initialize to null
			if (paperNode.has("publicationDate") && !paperNode.get("publicationDate").isNull()) {
				String publicationDateStr = paperNode.get("publicationDate").asText();
				
				if (!publicationDateStr.isEmpty() && !publicationDateStr.equalsIgnoreCase("null")) {
					publishedDate = LocalDate.parse(publicationDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
				}
			}
	
			String abstractText = paperNode.get("abstract").asText("");
			String sourceLink = paperNode.get("url").asText();
			if (paperNode.has("openAccessPdf") && paperNode.get("openAccessPdf").has("url")) {
				sourceLink = paperNode.get("openAccessPdf").get("url").asText();
			}

			int citationsCount = paperNode.get("citationCount").asInt(0);
			int viewsCount = 0; 

			LocalDateTime publishedDateTime = null;
			if (publishedDate != null) {
				publishedDateTime = publishedDate.atStartOfDay();
			}

			ResearchPaper researchPaper = new ResearchPaper(
				paperId,
				title,
				publishedDateTime,
				abstractText,
				null,
				sourceLink,
				citationsCount,
				viewsCount
			);
			researchPaperService.saveResearchPaper(researchPaper);

			// extract authors from the 'authors' array in the paperNode
			Set<Author> authors = new HashSet<>();
			if (paperNode.has("authors")) {
				for (JsonNode authorNode : paperNode.get("authors")) {

					String authorName = authorNode.get("name").asText();
					Author author = new Author(authorName);
					
					author.getResearchPapers().add(researchPaper);
					authors.add(author);

					authorService.saveAuthor(author);
				}
			}

			// update authors to the researchPaper
			researchPaper.setAuthors(authors);
			researchPaperService.updateResearchPaper(researchPaper.getPaperId(), researchPaper);
		}

		// first get paper ids into json array
		// set generated response to token response and save
		// for TokenResponse
		JSONArray fetchedPapersIds = DataService.getPaperIDs(fetchedPapers);
		tokenResponse.setGeneratedResponse(fetchedPapersIds.toString());
		tokenResponseService.saveTokenResponse(tokenResponse);

		// create app response. will save after
		AppResponse appResponse = new AppResponse();
		appResponse.setUserPrompt(userPrompt);
		appResponseService.saveAppResponse(appResponse);

		// save graph
		Graph graph = new Graph();
		graph.setAppResponse(appResponse);
		if (userPrompt.getGraphViewType() != GraphViewType.NONE && userPrompt.getGraphViewType() == GraphViewType.CITATION) {
			graph.setGraphType(GraphType.CITATION);
		} else if (userPrompt.getGraphViewType() != GraphViewType.NONE && userPrompt.getGraphViewType() == GraphViewType.OPINION) {
			graph.setGraphType(GraphType.OPINION);
		}
		graphService.saveGraph(graph);

		// ------------ Citation-based ------------------
		
		// semantic (2): get the top cited papers's next 5 citations (ideally some papers cite each other)
		ObjectNode fetchedCitations = objectMapper.createObjectNode();
		// System.out.println(fetchedPapers);
		for (JsonNode paper: fetchedPapers) {
			try {
				String paperID = paper.get("paperId").asText();
				JsonNode citation = semanticService.citationSearch(paperID).getBody();

				fetchedCitations.set(paperID, citation);
				
				Thread.sleep(1000); // semantic limit is 1rps
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}

		// if citiation graph, then create citationedge
		if (userPrompt.getGraphViewType() != GraphViewType.NONE && userPrompt.getGraphViewType() == GraphViewType.CITATION) {
			fetchedCitations.fieldNames().forEachRemaining(firstPaperId -> {
				ResearchPaper firstResearchPaper = researchPaperService.getResearchPaperBySemanticPaperId(firstPaperId);

				JsonNode citingPapers = fetchedCitations.get(firstPaperId);
				citingPapers.forEach(citingPaperNode -> {
					String secondPaperId = citingPaperNode.get("citingPaper").get("paperId").asText();
					ResearchPaper secondResearchPaper = researchPaperService.getResearchPaperBySemanticPaperId(secondPaperId);

					// second paper does not exist
					// NOTE: there is less information here, they lack title, etc.
					if (secondResearchPaper == null) {
						secondResearchPaper = new ResearchPaper();
						secondResearchPaper.setSemanticPaperId(secondPaperId);
						secondResearchPaper.setAbstractText(citingPaperNode.get("citingPaper").get("abstract").asText(""));
						secondResearchPaper.setCitationsCount(citingPaperNode.get("citingPaper").get("citationCount").asInt(0));
						String sourceLink = "";
						if (citingPaperNode.has("openAccessPdf") && citingPaperNode.get("openAccessPdf").has("url")) {
							sourceLink = citingPaperNode.get("citingPaper").get("openAccessPdf").get("url").asText();
						}
						secondResearchPaper.setSourceLink(sourceLink);
						
						researchPaperService.saveResearchPaper(secondResearchPaper);
					}

					// create a new CitationEdge
					CitationEdge citationEdge = new CitationEdge(graph, firstResearchPaper, secondResearchPaper);
					citationEdgeService.saveCitationEdge(citationEdge);
				});
			});
		}

		// ------------ Opinion-based ------------------
		
		// gemini (2): tuned_prompt for top 20 papers, sort them by support/oppose, and get the summarized opinions
		String opinionPrompt = String.format(
				"Here is the user's opinionated prompt: '%s' and a JSON list of relevant research papers: '%s',"
				 + "I need you to sort the list of papers into 2 categories either 'supporting' or 'opposing' the user-prompt;" 
				 + "then under supporting or opposing categories, further categorizes the papers by summarizing the key opinions, and list papers with that opinion under that key opinion"
				 + "strucutre response like a JSON object without any formatting (must to be able to be parsed directly so no invalid characters) that may look like: 'supporting: {opinion1: [paper1ID, paper2ID...], opinion2: [paper3ID...], ...}, opposing: {opinion1: [paper4ID], opinion2: [paper5ID...], ...}'"
				 , userPrompt.getSearchPrompt(), fetchedPapers);

		String categorizedResponseBody = geminiService.callApi(opinionPrompt, "AIzaSyDLmB0f1-lmo2-WH9Dif0fC32t0_Z9Hpuo");

		// save app response
		appResponse.setGeneratedResponse(categorizedResponseBody);
		appResponse.setGeneratedDateTime(LocalDateTime.now());
		appResponseService.updateAppResponse(appResponse.getId(), appResponse);

		ObjectNode categorizedResponseJSON = categorizedResponseFormatting(categorizedResponseBody).getBody(); // response formatting

		// if opinion graph, then create opinions, opinionrelatedpapers and opinionedges
		if (userPrompt.getGraphViewType() != GraphViewType.NONE && userPrompt.getGraphViewType() == GraphViewType.OPINION) {
			Iterator<String> fieldNames = categorizedResponseJSON.fieldNames();
			while (fieldNames.hasNext()) {
				String key = fieldNames.next();
				JsonNode opinionCategoryNode = categorizedResponseJSON.get(key);

				String opinionType = key.equals("supporting") ? "supporting" : "opposing";

				if (opinionCategoryNode != null && opinionCategoryNode.isObject()) {
					Iterator<String> categoryNames = opinionCategoryNode.fieldNames();
					while (categoryNames.hasNext()) {
						String opinionVal = categoryNames.next();

						// save new opinion
						Opinion opinion = new Opinion(); 
						opinion.setOpinionVal(opinionVal);

						if (opinionType.equals("supporting")) {
							opinion.setOpinionType(OpinionType.SUPPORTING);
						} else {
							opinion.setOpinionType(OpinionType.OPPOSING);
						}


						opinionService.saveOpinion(opinion);

						// save opinionrelatedpaper object
						JsonNode paperIdsArray = opinionCategoryNode.get(opinionVal);
						if (paperIdsArray.isArray()) {
							for (JsonNode paperIdNode : paperIdsArray) {
								String paperId = paperIdNode.asText();

								// find researchpaper related to the paperId
								ResearchPaper researchPaper = researchPaperService.getResearchPaperBySemanticPaperId(paperId);

								// create opinionrelatedpaper object
								OpinionRelatedPapers opinionRelatedPaper = new OpinionRelatedPapers(opinion, researchPaper);
								opinionRelatedPaperService.saveOpinionRelatedPapers(opinionRelatedPaper);
							}
						}
						
						// save opinion edge
						OpinionEdgeId opinionEdgeId = new OpinionEdgeId(0, graph.getId(), opinion.getId());
						opinionEdgeId.setEdgeId(opinionEdgeIdService.generateRandomEdgeId());
						OpinionEdge opinionEdge = new OpinionEdge();
						opinionEdge.setId(opinionEdgeId);
						opinionEdge.setGraph(graph);
						opinionEdge.setOpinion(opinion);
						opinionEdgeService.saveOpinionEdge(opinionEdge);
					}
				}
			}
		}

		if (categorizedResponseJSON != null) {
			categorizedResponseJSON.putPOJO("Papers", fetchedPapers);
			categorizedResponseJSON.putPOJO("Citations", fetchedCitations);

			try {
				return ResponseEntity.ok(objectMapper.writeValueAsString(categorizedResponseJSON)); // final output: JSON list of papers + support/opposing
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No gemini response (2) received.");
			} 
		} 
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	public ResponseEntity<ObjectNode> categorizedResponseFormatting(String responseBody) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(responseBody);
	
			String textNode = rootNode.path("candidates")
				.get(0).path("content").path("parts")
				.get(0).path("text").asText();
	
			textNode = textNode.replace("```json", "")
								.replaceAll("\n", "")	.replaceAll("\\\\", "")
								.replace("```", "").trim();
	
			ObjectNode formattedNode = (ObjectNode) mapper.readTree(textNode);
	
			return ResponseEntity.ok(formattedNode);

		} catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

	public ResponseEntity<ObjectNode> responseFormatting(String responseBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
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

		} catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
	}
}