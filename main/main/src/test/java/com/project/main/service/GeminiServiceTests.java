package com.project.main.service;

import com.project.main.service.GeminiService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeminiServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiService geminiService;

    private String geminiKey;
    private String apiUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        geminiKey = "test-key";
        apiUrl = String.format("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=%s", geminiKey);
    }

    @Test
    public void testCallApiSuccess() {
        String prompt = "Hello, Gemini!";
        String responseBody = "{\"response\": \"Hi there!\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"text\": \"" + prompt + "\"}", headers);
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        String result = geminiService.callApi(prompt, geminiKey);

        assertNotNull(result);
        assertEquals(responseBody, result);
    }


    @Test
    public void testCallApiFailure() {
        String prompt = "Hello, Gemini!";

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("API request failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            geminiService.callApi(prompt, geminiKey);
        });

        assertEquals("API request failed", exception.getMessage());
    }
}