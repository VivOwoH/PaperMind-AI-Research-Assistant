package com.project.main.service;

import com.project.main.entity.UserPrompt;
import com.project.main.repository.UserPromptRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPromptServiceTests {

    @Mock
    private UserPromptRepo userPromptRepo;

    @InjectMocks
    private UserPromptService userPromptService;

    private UserPrompt userPrompt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // sample UserPrompt object
        userPrompt = new UserPrompt();
        userPrompt.setId(1);
        userPrompt.setSearchPrompt("Sample prompt");
        userPrompt.setDateTime(LocalDateTime.now());
    }

    @Test
    void testGetAllUserPrompts() {
        // mock 
        List<UserPrompt> mockPrompts = Arrays.asList(userPrompt);
        when(userPromptRepo.findAll()).thenReturn(mockPrompts);

        List<UserPrompt> result = this.userPromptService.getAllUserPrompts();
        assertEquals(1, result.size());
        verify(userPromptRepo, times(1)).findAll();
    }

    @Test
    void testGetUserPromptById() {
        when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPrompt));

        UserPrompt result = this.userPromptService.getUserPromptById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(userPromptRepo, times(1)).findById(1);
    }

    @Test
    void testGetUserPromptByIdNotFound() {
        when(userPromptRepo.findById(2)).thenReturn(Optional.empty());

        UserPrompt result = this.userPromptService.getUserPromptById(2);
        assertNull(result);
        verify(userPromptRepo, times(1)).findById(2);
    }

    @Test
    void testSaveUserPromptSuccess() {
        when(userPromptRepo.save(userPrompt)).thenReturn(userPrompt);

        UserPrompt result = this.userPromptService.saveUserPrompt(userPrompt);
        assertNotNull(result);
        verify(userPromptRepo, times(1)).save(userPrompt);
    }

    @Test
    void testSaveUserPromptWithOriginalNullDateTime() {
        userPrompt.setDateTime(null);
        when(userPromptRepo.save(userPrompt)).thenReturn(userPrompt);

        UserPrompt result = this.userPromptService.saveUserPrompt(userPrompt);
        assertNotNull(result.getDateTime());
        verify(userPromptRepo, times(1)).save(userPrompt);
    }

    @Test
    void testUpdateUserPromptSuccess() {
        // new user prompt details
        UserPrompt updatedPrompt = new UserPrompt();
        updatedPrompt.setId(1);
        updatedPrompt.setSearchPrompt("Updated prompt");
        when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPrompt));
        when(userPromptRepo.save(userPrompt)).thenReturn(userPrompt);

        UserPrompt result = this.userPromptService.updateUserPrompt(1, updatedPrompt);
        assertNotNull(result);
        assertEquals("Updated prompt", result.getSearchPrompt());
        verify(userPromptRepo, times(1)).findById(1);
        verify(userPromptRepo, times(1)).save(userPrompt);
    }

    @Test
    void testUpdateUserPromptIdDoesNotExist() {
        UserPrompt updatedPrompt = new UserPrompt();
        updatedPrompt.setId(2); 
        when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPrompt));

        UserPrompt result = this.userPromptService.updateUserPrompt(1, updatedPrompt);

        assertNull(result);
        verify(userPromptRepo, times(1)).findById(1);
        verify(userPromptRepo, times(0)).save(any());
    }

    @Test
    void testUpdateUserPromptNotFound() {
        UserPrompt updatedPrompt = new UserPrompt();
        updatedPrompt.setId(1);
        when(userPromptRepo.findById(1)).thenReturn(Optional.empty());

        UserPrompt result = this.userPromptService.updateUserPrompt(1, updatedPrompt);
        assertNull(result);
        verify(userPromptRepo, times(1)).findById(1);
    }

    @Test
    void testDeleteUserPromptById() {
        this.userPromptService.deleteUserPromptById(1);

        verify(userPromptRepo, times(1)).deleteById(1);
    }
}
