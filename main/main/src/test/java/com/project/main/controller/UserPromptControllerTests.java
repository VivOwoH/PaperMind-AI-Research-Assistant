package com.project.main.controller;

import com.project.main.entity.UserPrompt;
import com.project.main.service.UserPromptService;

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

class UserPromptControllerTests {

    @Mock
    private UserPromptService userPromptService;

    @InjectMocks
    private UserPromptController userPromptController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUserPrompts() {
        UserPrompt prompt1 = new UserPrompt(); 
        UserPrompt prompt2 = new UserPrompt();
        List<UserPrompt> prompts = Arrays.asList(prompt1, prompt2);

        when(userPromptService.getAllUserPrompts()).thenReturn(prompts);

        ResponseEntity<List<UserPrompt>> response = userPromptController.getAllUserPrompts();
        assertEquals(OK, response.getStatusCode());
        assertEquals(prompts, response.getBody());
        verify(userPromptService, times(1)).getAllUserPrompts();
    }

    @Test
    void testGetUserPromptById() {
        UserPrompt prompt = new UserPrompt(); 
        prompt.setId(1);
        when(userPromptService.getUserPromptById(1)).thenReturn(prompt);

        ResponseEntity<UserPrompt> response = userPromptController.getUserPromptById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals(prompt, response.getBody());
        verify(userPromptService, times(1)).getUserPromptById(1);
    }

    @Test
    void testSaveUserPrompt() {
        UserPrompt prompt = new UserPrompt();
        when(userPromptService.saveUserPrompt(prompt)).thenReturn(prompt);

        ResponseEntity<UserPrompt> response = userPromptController.saveUserPrompt(prompt);
        assertEquals(OK, response.getStatusCode());
        assertEquals(prompt, response.getBody());
        verify(userPromptService, times(1)).saveUserPrompt(prompt);
    }

    @Test
    void testUpdateUserPromptSuccess() {
        UserPrompt prompt = new UserPrompt();
        when(userPromptService.updateUserPrompt(1, prompt)).thenReturn(prompt);

        ResponseEntity<UserPrompt> response = userPromptController.updateUserPrompt(1, prompt);
        assertEquals(OK, response.getStatusCode());
        assertEquals(prompt, response.getBody());
        verify(userPromptService, times(1)).updateUserPrompt(1, prompt);
    }

    @Test
    void testUpdateUserPromptNotFound() {
        UserPrompt prompt = new UserPrompt();
        when(userPromptService.updateUserPrompt(1, prompt)).thenReturn(null);

        ResponseEntity<UserPrompt> response = userPromptController.updateUserPrompt(1, prompt);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(userPromptService, times(1)).updateUserPrompt(1, prompt);
    }

    @Test
    void testDeleteUserPromptById() {
        doNothing().when(userPromptService).deleteUserPromptById(1);

        ResponseEntity<String> response = userPromptController.deleteUserPromptById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted user prompt successfully", response.getBody());
        verify(userPromptService, times(1)).deleteUserPromptById(1);
    }
}
