package com.project.main.service;

import com.project.main.service.SemanticService;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SemanticServiceTests {

    @InjectMocks
    private SemanticService semanticService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPaperRelevanceSearchSuccess() {
        String query = "machine learning";

        String mockResponseBody = "{\"data\":[{\"title\":\"Paper 1\"},{\"title\":\"Paper 2\"}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(mockResponse);

        ResponseEntity<JsonNode> response = semanticService.paperRelevanceSearch(query);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().has(0));
    }

    @Test
    public void testPaperRelevanceSearchNoPapersFound() {
        String query = "nonexistent topic";

        String mockResponseBody = "{\"data\":[]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(mockResponse);

        ResponseEntity<JsonNode> response = semanticService.paperRelevanceSearch(query);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No papers found for the query.", response.getBody().get("message").asText());
    }

    @Test
    public void testCitationSearchSuccess() {
        String paperID = "12345";

        String mockResponseBody = "{\"data\":[{\"title\":\"Citation 1\"},{\"title\":\"Citation 2\"}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(mockResponse);

        ResponseEntity<JsonNode> response = semanticService.citationSearch(paperID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().has(0));
    }

    @Test
    public void testCitationSearchNoCitationsFound() {
        String paperID = "00000";

        String mockResponseBody = "{\"data\":[]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(mockResponse);

        ResponseEntity<JsonNode> response = semanticService.citationSearch(paperID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No citations found for the paper.", response.getBody().get("message").asText());
    }

    // @Test
    // public void testPaperRelevanceSearch_InternalServerError() {
    //     String query = "machine learning";

    //     when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
    //             .thenThrow(new RuntimeException("Internal Server Error"));

    //     ResponseEntity<JsonNode> response = semanticService.paperRelevanceSearch(query);

    //     assertNotNull(response);
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }

    // @Test
    // public void testCitationSearch_InternalServerError() {
    //     String paperID = "12345";

    //     when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
    //             .thenThrow(new RuntimeException("Internal Server Error"));

    //     ResponseEntity<JsonNode> response = semanticService.citationSearch(paperID);

    //     assertNotNull(response);
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    //     assertTrue(response.getBody().isEmpty());
    // }
}
