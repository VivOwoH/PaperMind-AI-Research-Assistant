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

    @Mock
    private UserPrompt userPromptOne;

    @Mock
    private UserPrompt userPromptTwo;

    @InjectMocks
    private UserPromptService userPromptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testGetAllUserPrompts() {
    //     // mock
    //     List<UserPrompt> prompts = Arrays.asList(userPromptOne, userPromptTwo);
    //     when(userPromptRepo.findAll()).thenReturn(prompts);

    //     // test
    //     List<UserPrompt> result = userPromptService.getAllUserPrompts();
    //     assertEquals(2, result.size());

    //     verify(userPromptRepo, times(1)).findAll();
    // }

    // @Test
    // void testGetUserPromptById_Exists() {
    //     // mock
    //     Integer currentId = userPromptOne.getId();
    //     when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPromptOne));

    //     // test
    //     UserPrompt result = userPromptService.getUserPromptById(1);
    //     assertNotNull(result);
    //     assertEquals(currentId, result.getId());

    //     verify(userPromptRepo, times(1)).findById(1);
    // }

    // @Test
    // void testGetUserPromptById_NotExists() {
    //     // mock
    //     when(userPromptRepo.findById(1)).thenReturn(Optional.empty());

    //     // test
    //     UserPrompt result = userPromptService.getUserPromptById(1);
    //     assertNull(result);

    //     verify(userPromptRepo, times(1)).findById(1);
    // }

    // // fails
    // @Test
    // void testSaveUserPrompt_WithNullDateTime() {
    //     // mock
    //     UserPrompt prompt = userPromptOne;
    //     when(userPromptRepo.save(any(UserPrompt.class))).thenAnswer(i -> {
    //         UserPrompt savedPrompt = i.getArgument(0);
    //         savedPrompt.setId(1);
    //         return savedPrompt;
    //     });

    //     // test
    //     UserPrompt result = userPromptService.saveUserPrompt(prompt);
    //     assertNotNull(result.getDateTime());
    //     assertEquals(1, result.getId());
        
    //     verify(userPromptRepo, times(1)).save(any(UserPrompt.class));
    // }

    // // fails
    // @Test
    // void testSaveUserPrompt_WithNonNullDateTime() {
    //     LocalDateTime dateTime = LocalDateTime.of(2023, 9, 15, 12, 0);
    //     UserPrompt prompt = userPromptOne;
    //     prompt.setDateTime(dateTime);

    //     when(userPromptRepo.save(any(UserPrompt.class))).thenAnswer(i -> {
    //         UserPrompt savedPrompt = i.getArgument(0);
    //         savedPrompt.setId(1);
    //         return savedPrompt;
    //     });

    //     UserPrompt result = userPromptService.saveUserPrompt(prompt);

    //     assertEquals(dateTime, result.getDateTime());
    //     assertEquals(1, result.getId());
    //     verify(userPromptRepo, times(1)).save(any(UserPrompt.class));
    // }

    // @Test
    // void testDeleteUserPromptById() {
    //     doNothing().when(userPromptRepo).deleteById(1);

    //     userPromptService.deleteUserPromptById(1);

    //     verify(userPromptRepo, times(1)).deleteById(1);
    // }
}