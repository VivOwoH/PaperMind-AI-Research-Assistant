package com.project.main.service;

import com.project.main.entity.TokenResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.repository.TokenResponseRepo;
import com.project.main.repository.UserPromptRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenResponseServiceTests {

    @Mock
    private TokenResponseRepo tokenResponseRepo;

    @Mock
    private UserPromptRepo userPromptRepo;

    @InjectMocks
    private TokenResponseService tokenResponseService;

    private TokenResponse tokenResponse;
    private UserPrompt userPrompt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userPrompt = new UserPrompt();
        userPrompt.setId(1);
        userPrompt.setSearchPrompt("Test UserPrompt");

        tokenResponse = new TokenResponse();
        tokenResponse.setId(1);
        tokenResponse.setUserPrompt(userPrompt);
    }

    @Test
    void testGetAllTokenResponses() {
        when(tokenResponseRepo.findAll()).thenReturn(Arrays.asList(tokenResponse));

        List<TokenResponse> result = tokenResponseService.getAllTokenResponses();
        assertEquals(1, result.size());
        verify(tokenResponseRepo, times(1)).findAll();
    }

    @Test
    void testGetTokenResponseById() {
        when(tokenResponseRepo.findById(1)).thenReturn(Optional.of(tokenResponse));

        TokenResponse result = tokenResponseService.getTokenResponseById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(tokenResponseRepo, times(1)).findById(1);
    }

    @Test
    void testGetTokenResponseByIdNotFound() {
        when(tokenResponseRepo.findById(2)).thenReturn(Optional.empty());

        TokenResponse result = tokenResponseService.getTokenResponseById(2);
        assertNull(result);
        verify(tokenResponseRepo, times(1)).findById(2);
    }

    @Test
    void testGetUserPromptByTokenResponseId() {
        when(tokenResponseRepo.findById(1)).thenReturn(Optional.of(tokenResponse));

        UserPrompt result = tokenResponseService.getUserPromptByTokenResponseId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(tokenResponseRepo, times(1)).findById(1);
    }

    @Test
    void testGetUserPromptByTokenResponseIdNotFound() {
        when(tokenResponseRepo.findById(2)).thenReturn(Optional.empty());

        UserPrompt result = tokenResponseService.getUserPromptByTokenResponseId(2);
        assertNull(result);
        verify(tokenResponseRepo, times(1)).findById(2);
    }

    @Test
    void testSaveTokenResponseSuccess() {
        when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPrompt));
        when(tokenResponseRepo.save(tokenResponse)).thenReturn(tokenResponse);

        TokenResponse result = tokenResponseService.saveTokenResponse(tokenResponse);
        assertNotNull(result);
        assertEquals(1, result.getUserPrompt().getId());
        verify(tokenResponseRepo, times(1)).save(tokenResponse);
    }

    @Test
    void testSaveTokenResponseUserPromptNotFound() {
        when(userPromptRepo.findById(2)).thenReturn(Optional.empty());

        tokenResponse.getUserPrompt().setId(2);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenResponseService.saveTokenResponse(tokenResponse);
        });
        assertEquals("UserPrompt with the given ID does not exist", exception.getMessage());
        verify(userPromptRepo, times(1)).findById(2);
        verify(tokenResponseRepo, times(0)).save(any());
    }

    @Test
    void testSaveTokenResponseUserPromptNotProvided() {
        tokenResponse.setUserPrompt(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenResponseService.saveTokenResponse(tokenResponse);
        });
        assertEquals("UserPrompt ID must be provided", exception.getMessage());
        verify(tokenResponseRepo, times(0)).save(any());
    }

    @Test
    void testUpdateTokenResponseSuccess() {
        TokenResponse updatedResponse = new TokenResponse();
        updatedResponse.setId(1);
        updatedResponse.setUserPrompt(userPrompt);

        when(tokenResponseRepo.findById(1)).thenReturn(Optional.of(tokenResponse));
        when(tokenResponseRepo.save(tokenResponse)).thenReturn(tokenResponse);

        TokenResponse result = tokenResponseService.updateTokenResponse(1, updatedResponse);
        assertNotNull(result);
        verify(tokenResponseRepo, times(1)).findById(1);
        verify(tokenResponseRepo, times(1)).save(tokenResponse);
    }

    @Test
    void testUpdateTokenResponseIdWrong() {
        TokenResponse updatedResponse = new TokenResponse();
        updatedResponse.setId(2); // Different ID from the existing tokenResponse

        when(tokenResponseRepo.findById(1)).thenReturn(Optional.of(tokenResponse));

        TokenResponse result = tokenResponseService.updateTokenResponse(1, updatedResponse);
        assertNull(result);
        verify(tokenResponseRepo, times(1)).findById(1);
        verify(tokenResponseRepo, times(0)).save(any());
    }

    @Test
    void testUpdateTokenResponseNotFound() {
        TokenResponse updatedResponse = new TokenResponse();
        updatedResponse.setId(1);

        when(tokenResponseRepo.findById(1)).thenReturn(Optional.empty());

        TokenResponse result = tokenResponseService.updateTokenResponse(1, updatedResponse);
        assertNull(result);
        verify(tokenResponseRepo, times(1)).findById(1);
        verify(tokenResponseRepo, times(0)).save(any());
    }

    @Test
    void testDeleteTokenResponseById() {
        tokenResponseService.deleteTokenResponseById(1);

        verify(tokenResponseRepo, times(1)).deleteById(1);
    }
}
