package com.project.main.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.main.entity.*;
import com.project.main.enums.GraphType;
import com.project.main.enums.GraphViewType;
import com.project.main.service.*;

@ActiveProfiles("dev")
@SpringBootTest
public class APIControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GeminiService geminiService;

    @Mock
    private SemanticService semanticService;

    @Mock
    private UserPromptService userPromptService;

    @Mock
    private TokenResponseService tokenResponseService;

    @Mock
    private AppResponseService appResponseService;

    @Mock
    private GraphService graphService;

    @Mock
    private AuthorService authorService;

    @Mock
    private ResearchPaperService researchPaperService;

    @Mock
    private CitationEdgeService citationEdgeService;

    @Mock
    private OpinionService opinionService;

    @Mock
    private OpinionRelatedPaperService opinionRelatedPaperService;

    @Mock
    private OpinionEdgeService opinionEdgeService;

    @InjectMocks
    @Spy
    private APIController apiController;

    @Value("${apiKey:dummyApiKey}")
    private String apiKey;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testMainAPIflowSuccess() throws Exception {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setSearchPrompt("Channel estimation with pilots is outdated");

        // responseFormatting method
        String geminiResponseKeywords = "{ \"candidates\": [ { \"content\": { \"parts\": [ { \"text\": \"{ \\\"keywords\\\": \\\"channel estimation\\\", \\\"sentiment\\\": \\\"positive\\\" }\" } ] } } ] }";
        when(geminiService.callApi(contains("extract key words"), any())).thenReturn(geminiResponseKeywords);

        ObjectNode responseJson = objectMapper.createObjectNode();
        responseJson.put("keywords", "channel estimation");
        responseJson.put("sentiment", "positive");
        doReturn(ResponseEntity.ok(responseJson)).when(apiController).responseFormatting(anyString());

        // categorisedResponseFormatting method
        String geminiResponseOpinion = "{ \"supporting\": { \"opinion1\": [\"paper1ID\"] }, \"opposing\": { \"opinion2\": [\"paper2ID\"] } }";
        when(geminiService.callApi(contains("Here is the user's opinionated prompt"), any())).thenReturn(geminiResponseOpinion);

        ObjectNode responseOpinionJson = objectMapper.createObjectNode();
        ObjectNode supporting = responseOpinionJson.putObject("supporting");
        supporting.putArray("opinion1").add("paper1ID");
        ObjectNode opposing = responseOpinionJson.putObject("opposing");
        opposing.putArray("opinion2").add("paper2ID");
        doReturn(ResponseEntity.ok(responseOpinionJson)).when(apiController).categorizedResponseFormatting(anyString());

        // other mocks
        when(semanticService.paperRelevanceSearch(anyString())).thenReturn(ResponseEntity.ok(objectMapper.createArrayNode()));
        when(userPromptService.saveUserPrompt(any(UserPrompt.class))).thenReturn(userPrompt);
        when(researchPaperService.saveResearchPaper(any())).thenReturn(new ResearchPaper());

        ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testMainAPIflowGeminiFailure() throws Exception {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setSearchPrompt("Test prompt");

        // simulate Gemini failure (return null or empty response)
        when(geminiService.callApi(anyString(), any())).thenReturn(null);

        ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("No gemini response (1) received.", response.getBody());
    }

    @Test
    public void testMainAPIflowSemanticFailure() throws Exception {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setSearchPrompt("Test prompt");

        String geminiResponse = "{ \"candidates\": [{ \"content\": { \"parts\": [{ \"text\": \"{\\\"keywords\\\": \\\"test keyword\\\", \\\"sentiment\\\": \\\"positive\\\"}\" }]}}]}";
        when(geminiService.callApi(anyString(), any())).thenReturn(geminiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseJson = objectMapper.createObjectNode();
        responseJson.put("keywords", "test keyword");
        responseJson.put("sentiment", "positive");
        when(apiController.responseFormatting(geminiResponse)).thenReturn(ResponseEntity.ok(responseJson));

        when(semanticService.paperRelevanceSearch(anyString())).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("No semantic response (1) received.", response.getBody());
    }

    @Test
    public void testCategorizedResponseFormatting() throws Exception {
        String geminiResponse = "{ \"candidates\": [{ \"content\": { \"parts\": [{ \"text\": \"{\\\"supporting\\\": {\\\"opinion1\\\": [\\\"paper1ID\\\"]}}\" }]}}]}";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode expectedFormattedNode = objectMapper.createObjectNode();
        ObjectNode supportingNode = expectedFormattedNode.putObject("supporting");
        supportingNode.putArray("opinion1").add("paper1ID");

        ResponseEntity<ObjectNode> formattedResponse = apiController.categorizedResponseFormatting(geminiResponse);

        assertNotNull(formattedResponse);
        assertEquals(HttpStatus.OK, formattedResponse.getStatusCode());
        assertEquals(expectedFormattedNode, formattedResponse.getBody());
    }


@Test
public void testNullResponseFromCategorizedResponseFormatting() throws Exception {
    // Simulate a case where categorizedResponseFormatting returns null
    when(apiController.categorizedResponseFormatting(anyString())).thenReturn(null);

    UserPrompt userPrompt = new UserPrompt();
    userPrompt.setSearchPrompt("Test prompt");

    ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

    assertNotNull(response);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
}

@Test
public void testNullResponseFromResponseFormatting() throws Exception {
    // Simulate a case where responseFormatting returns null
    when(apiController.responseFormatting(anyString())).thenReturn(null);

    UserPrompt userPrompt = new UserPrompt();
    userPrompt.setSearchPrompt("Test prompt");

    ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

    assertNotNull(response);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
}

@Test
public void testNotNullResponseFromResponseFormatting() throws Exception {
    UserPrompt userPrompt = new UserPrompt();
    userPrompt.setSearchPrompt("Test prompt");

    // Set up a mock response for responseFormatting
    ObjectNode responseJson = objectMapper.createObjectNode();
    responseJson.put("keywords", "test");
    responseJson.put("sentiment", "neutral");
    doReturn(ResponseEntity.ok(responseJson)).when(apiController).responseFormatting(anyString());

    ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

    assertNotNull(response);
    assertNotNull(response.getBody()); // Check that the body is not null
}
 

    @Test
    public void testNotNullResponseFromCategorizedResponseFormatting() throws Exception {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setSearchPrompt("Test prompt");

        // Set up a mock response for categorizedResponseFormatting
        ObjectNode responseJson = objectMapper.createObjectNode();
        responseJson.put("supporting", "testSupporting");
        responseJson.put("opposing", "testOpposing");
        doReturn(ResponseEntity.ok(responseJson)).when(apiController).categorizedResponseFormatting(anyString());

        ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

        assertNotNull(response);
        assertNotNull(response.getBody()); 
    }

    @Test
    public void testMainAPIflowEmptyUserPrompt() throws Exception {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setSearchPrompt(""); 

        ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

        assertNotNull(response);
    }

    @Test
    public void testResponseFormattingWithInvalidJson() throws Exception {
        String invalidJson = "Invalid JSON string";

        ResponseEntity<ObjectNode> response = apiController.responseFormatting(invalidJson);

        assertNotNull(response);
    }

    @Test
    public void testCategorizedResponseFormattingWithEmptyInput() throws Exception {
        String emptyJson = "{}"; 

        ResponseEntity<ObjectNode> response = apiController.categorizedResponseFormatting(emptyJson);

        assertNotNull(response);
    }

    @Test
    public void testValidCategorizedResponseFormatting() throws Exception {
        String validInput = "{ \"supporting\": { \"opinion1\": [\"paper1ID\"] }, \"opposing\": { \"opinion2\": [\"paper2ID\"] } }";

        ObjectNode expectedOutput = objectMapper.createObjectNode();
        ObjectNode supportingNode = expectedOutput.putObject("supporting");
        supportingNode.putArray("opinion1").add("paper1ID");
        ObjectNode opposingNode = expectedOutput.putObject("opposing");
        opposingNode.putArray("opinion2").add("paper2ID");

        doReturn(ResponseEntity.ok(expectedOutput)).when(apiController).categorizedResponseFormatting(validInput);

        ResponseEntity<ObjectNode> response = apiController.categorizedResponseFormatting(validInput);

        assertNotNull(response);
    }

    @Test
    public void testMainAPIflowWithoutTokenService() throws Exception {
        UserPrompt userPrompt = new UserPrompt();
        userPrompt.setSearchPrompt("Test without token service");

        ResponseEntity<String> response = apiController.mainAPIflow(userPrompt);

        assertNotNull(response);
    }



}
