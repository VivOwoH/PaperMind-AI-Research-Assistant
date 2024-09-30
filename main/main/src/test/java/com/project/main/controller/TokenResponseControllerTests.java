package com.project.main.controller;

import com.project.main.entity.TokenResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.service.TokenResponseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class TokenResponseControllerTests {

    @Mock
    private TokenResponseService tokenResponseService;

    @InjectMocks
    private TokenResponseController tokenResponseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTokenResponses() {
        TokenResponse response1 = new TokenResponse();
        TokenResponse response2 = new TokenResponse();
        List<TokenResponse> responses = Arrays.asList(response1, response2);

        when(tokenResponseService.getAllTokenResponses()).thenReturn(responses);

        ResponseEntity<List<TokenResponse>> response = tokenResponseController.getAllTokenResponses();

        assertEquals(OK, response.getStatusCode());
        assertEquals(responses, response.getBody());
        verify(tokenResponseService, times(1)).getAllTokenResponses();
    }

    @Test
    void testGetTokenResponseById() {
        TokenResponse tokenResponse = new TokenResponse();
        when(tokenResponseService.getTokenResponseById(1)).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = tokenResponseController.getTokenResponseById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(tokenResponse, response.getBody());
        verify(tokenResponseService, times(1)).getTokenResponseById(1);
    }

    @Test
    void testGetUserPromptByTokenResponse() {
        UserPrompt userPrompt = new UserPrompt();
        when(tokenResponseService.getUserPromptByTokenResponseId(1)).thenReturn(userPrompt);

        ResponseEntity<UserPrompt> response = tokenResponseController.getUserPromptByTokenResponse(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(userPrompt, response.getBody());
        verify(tokenResponseService, times(1)).getUserPromptByTokenResponseId(1);
    }

    @Test
    void testSaveTokenResponse() {
        TokenResponse tokenResponse = new TokenResponse();
        when(tokenResponseService.saveTokenResponse(tokenResponse)).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = tokenResponseController.saveTokenResponse(tokenResponse);

        assertEquals(OK, response.getStatusCode());
        assertEquals(tokenResponse, response.getBody());
        verify(tokenResponseService, times(1)).saveTokenResponse(tokenResponse);
    }

    @Test
    void testUpdateTokenResponseSuccess() {
        TokenResponse tokenResponse = new TokenResponse();
        when(tokenResponseService.updateTokenResponse(1, tokenResponse)).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = tokenResponseController.updateTokenResponse(1, tokenResponse);

        assertEquals(OK, response.getStatusCode());
        assertEquals(tokenResponse, response.getBody());
        verify(tokenResponseService, times(1)).updateTokenResponse(1, tokenResponse);
    }

    @Test
    void testUpdateTokenResponseNotFound() {
        TokenResponse tokenResponse = new TokenResponse();
        when(tokenResponseService.updateTokenResponse(1, tokenResponse)).thenReturn(null);

        ResponseEntity<TokenResponse> response = tokenResponseController.updateTokenResponse(1, tokenResponse);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(tokenResponseService, times(1)).updateTokenResponse(1, tokenResponse);
    }

    @Test
    void testDeleteTokenResponseById() {
        doNothing().when(tokenResponseService).deleteTokenResponseById(1);

        ResponseEntity<String> response = tokenResponseController.deleteTokenResponseById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted token response successfully", response.getBody());
        verify(tokenResponseService, times(1)).deleteTokenResponseById(1);
    }
}
