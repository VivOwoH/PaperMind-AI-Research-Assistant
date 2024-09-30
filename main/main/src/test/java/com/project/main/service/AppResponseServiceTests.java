package com.project.main.service;

import com.project.main.entity.AppResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.repository.AppResponseRepo;
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

class AppResponseServiceTests {

    @Mock
    private AppResponseRepo appResponseRepo;

    @Mock
    private UserPromptRepo userPromptRepo;

    @InjectMocks
    private AppResponseService appResponseService;

    private AppResponse appResponse;
    private UserPrompt userPrompt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userPrompt = new UserPrompt();
        userPrompt.setId(1);
        userPrompt.setSearchPrompt("Sample UserPrompt");

        appResponse = new AppResponse();
        appResponse.setId(1);
        appResponse.setGeneratedDateTime(LocalDateTime.now());
        appResponse.setUserPrompt(userPrompt);
    }

    @Test
    void testGetAllAppResponses() {
        when(appResponseRepo.findAll()).thenReturn(Arrays.asList(appResponse));

        List<AppResponse> result = appResponseService.getAllAppResponses();
        assertEquals(1, result.size());
        verify(appResponseRepo, times(1)).findAll();
    }

    @Test
    void testGetAppResponseById() {
        when(appResponseRepo.findById(1)).thenReturn(Optional.of(appResponse));

        AppResponse result = appResponseService.getAppResponseById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(appResponseRepo, times(1)).findById(1);
    }

    @Test
    void testGetAppResponseByIdNotFound() {
        when(appResponseRepo.findById(2)).thenReturn(Optional.empty());

        AppResponse result = appResponseService.getAppResponseById(2);
        assertNull(result);
        verify(appResponseRepo, times(1)).findById(2);
    }

    @Test
    void testGetUserPromptByAppResponseId() {
        when(appResponseRepo.findById(1)).thenReturn(Optional.of(appResponse));

        UserPrompt result = appResponseService.getUserPromptByAppResponseId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(appResponseRepo, times(1)).findById(1);
    }

    @Test
    void testGetUserPromptByAppResponseIdNotFound() {
        when(appResponseRepo.findById(2)).thenReturn(Optional.empty());

        UserPrompt result = appResponseService.getUserPromptByAppResponseId(2);
        assertNull(result);
        verify(appResponseRepo, times(1)).findById(2);
    }

    @Test
    void testSaveAppResponse() {
        when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPrompt));
        when(appResponseRepo.save(appResponse)).thenReturn(appResponse);

        AppResponse result = appResponseService.saveAppResponse(appResponse);
        assertNotNull(result);
        assertEquals(1, result.getUserPrompt().getId());
        verify(appResponseRepo, times(1)).save(appResponse);
    }

    @Test
    void testSaveAppResponseWithGeneratedDateTimeNull() {
        appResponse.setGeneratedDateTime(null);
        when(userPromptRepo.findById(1)).thenReturn(Optional.of(userPrompt));
        when(appResponseRepo.save(appResponse)).thenReturn(appResponse);

        AppResponse result = appResponseService.saveAppResponse(appResponse);
        assertNotNull(result.getGeneratedDateTime());
        verify(appResponseRepo, times(1)).save(appResponse);
    }

    @Test
    void testSaveAppResponseUserPromptNotFound() {
        when(userPromptRepo.findById(2)).thenReturn(Optional.empty());
        appResponse.getUserPrompt().setId(2);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appResponseService.saveAppResponse(appResponse);
        });

        assertEquals("UserPrompt with the given ID does not exist", exception.getMessage());
        verify(userPromptRepo, times(1)).findById(2);
        verify(appResponseRepo, times(0)).save(any());
    }

    @Test
    void testSaveAppResponseUserPromptNotProvided() {
        appResponse.setUserPrompt(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appResponseService.saveAppResponse(appResponse);
        });

        assertEquals("UserPrompt ID must be provided", exception.getMessage());
        verify(appResponseRepo, times(0)).save(any());
    }

    @Test
    void testUpdateAppResponseSuccess() {
        AppResponse updatedAppResponse = new AppResponse();
        updatedAppResponse.setId(1);
        updatedAppResponse.setUserPrompt(userPrompt);

        when(appResponseRepo.findById(1)).thenReturn(Optional.of(appResponse));
        when(appResponseRepo.save(appResponse)).thenReturn(appResponse);

        AppResponse result = appResponseService.updateAppResponse(1, updatedAppResponse);
        assertNotNull(result);
        verify(appResponseRepo, times(1)).findById(1);
        verify(appResponseRepo, times(1)).save(appResponse);
    }

    @Test
    void testUpdateAppResponseIdWrong() {
        AppResponse updatedAppResponse = new AppResponse();
        updatedAppResponse.setId(2); 

        when(appResponseRepo.findById(1)).thenReturn(Optional.of(appResponse));

        AppResponse result = appResponseService.updateAppResponse(1, updatedAppResponse);
        assertNull(result);
        verify(appResponseRepo, times(1)).findById(1);
        verify(appResponseRepo, times(0)).save(any());
    }

    @Test
    void testUpdateAppResponseNotFound() {
        AppResponse updatedAppResponse = new AppResponse();
        updatedAppResponse.setId(1);

        when(appResponseRepo.findById(1)).thenReturn(Optional.empty());

        AppResponse result = appResponseService.updateAppResponse(1, updatedAppResponse);
        assertNull(result);
        verify(appResponseRepo, times(1)).findById(1);
        verify(appResponseRepo, times(0)).save(any());
    }

    @Test
    void testDeleteAppResponseById() {
        appResponseService.deleteAppResponseById(1);

        verify(appResponseRepo, times(1)).deleteById(1);
    }
}
