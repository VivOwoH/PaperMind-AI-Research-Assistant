package com.project.main.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.main.service.GeminiService;

@SpringBootTest
@AutoConfigureMockMvc
class AISummaryControllerTest {

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private AISummaryController aiSummaryController;

    private MockMvc mockMvc;

    @Value("${apiKey}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(aiSummaryController).build();
    }

    @Test
    void testGenerateAISummaryFailure() throws Exception {
        when(geminiService.callApi(anyString(), anyString())).thenReturn(null);

        String requestBody = "This is a sample abstract.";
        
        mockMvc.perform(post("/api/generate-summary")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("No gemini response (1) received."));
    }

    @Test
    void testGenerateAISummaryInvalidJson() throws Exception {
        String invalidJsonResponse = "{ \"candidates\": [] }";
        when(geminiService.callApi(anyString(), anyString())).thenReturn(invalidJsonResponse);

        String requestBody = "This is a sample abstract.";
        
        mockMvc.perform(post("/api/generate-summary")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("No gemini response (1) received."));
    }
}
