package com.project.main.controller;

import com.project.main.entity.AppResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.service.AppResponseService;

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

class AppResponseControllerTest {

    @Mock
    private AppResponseService appResponseService;

    @InjectMocks
    private AppResponseController appResponseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAppResponses() {
        AppResponse response1 = new AppResponse();
        AppResponse response2 = new AppResponse();
        List<AppResponse> responses = Arrays.asList(response1, response2);

        when(appResponseService.getAllAppResponses()).thenReturn(responses);

        ResponseEntity<List<AppResponse>> response = appResponseController.getAllAppResponses();
        assertEquals(OK, response.getStatusCode());
        assertEquals(responses, response.getBody());
        verify(appResponseService, times(1)).getAllAppResponses();
    }

    @Test
    void testGetAppResponseById() {
        AppResponse appResponse = new AppResponse();
        when(appResponseService.getAppResponseById(1)).thenReturn(appResponse);

        ResponseEntity<AppResponse> response = appResponseController.getAppResponseById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals(appResponse, response.getBody());
        verify(appResponseService, times(1)).getAppResponseById(1);
    }

    @Test
    void testGetUserPromptByAppResponse() {
        UserPrompt userPrompt = new UserPrompt();
        when(appResponseService.getUserPromptByAppResponseId(1)).thenReturn(userPrompt);

        ResponseEntity<UserPrompt> response = appResponseController.getUserPromptByAppResponse(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals(userPrompt, response.getBody());
        verify(appResponseService, times(1)).getUserPromptByAppResponseId(1);
    }

    @Test
    void testSaveAppResponse() {
        AppResponse appResponse = new AppResponse();
        when(appResponseService.saveAppResponse(appResponse)).thenReturn(appResponse);

        ResponseEntity<AppResponse> response = appResponseController.saveAppResponse(appResponse);
        assertEquals(OK, response.getStatusCode());
        assertEquals(appResponse, response.getBody());
        verify(appResponseService, times(1)).saveAppResponse(appResponse);
    }

    @Test
    void testUpdateAppResponseSuccess() {
        AppResponse appResponse = new AppResponse();
        when(appResponseService.updateAppResponse(1, appResponse)).thenReturn(appResponse);

        ResponseEntity<AppResponse> response = appResponseController.updateAppResponse(1, appResponse);
        assertEquals(OK, response.getStatusCode());
        assertEquals(appResponse, response.getBody());
        verify(appResponseService, times(1)).updateAppResponse(1, appResponse);
    }

    @Test
    void testUpdateAppResponseNotFound() {
        AppResponse appResponse = new AppResponse();
        when(appResponseService.updateAppResponse(1, appResponse)).thenReturn(null);

        ResponseEntity<AppResponse> response = appResponseController.updateAppResponse(1, appResponse);
        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(appResponseService, times(1)).updateAppResponse(1, appResponse);
    }

    @Test
    void testDeleteAppResponseById() {
        doNothing().when(appResponseService).deleteAppResponseById(1);

        ResponseEntity<String> response = appResponseController.deleteAppResponseById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted app response successfully", response.getBody());
        verify(appResponseService, times(1)).deleteAppResponseById(1);
    }
}
