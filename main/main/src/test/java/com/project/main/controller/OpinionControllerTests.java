package com.project.main.controller;

import com.project.main.entity.Opinion;
import com.project.main.service.OpinionService;

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

class OpinionControllerTests {

    @Mock
    private OpinionService opinionService;

    @InjectMocks
    private OpinionController opinionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOpinions() {
        Opinion opinion1 = new Opinion();
        Opinion opinion2 = new Opinion();
        List<Opinion> opinions = Arrays.asList(opinion1, opinion2);

        when(opinionService.getAllOpinions()).thenReturn(opinions);

        ResponseEntity<List<Opinion>> response = opinionController.getAllOpinions();

        assertEquals(OK, response.getStatusCode());
        assertEquals(opinions, response.getBody());
        verify(opinionService, times(1)).getAllOpinions();
    }

    @Test
    void testGetOpinionById() {
        Opinion opinion = new Opinion();
        when(opinionService.getOpinionById(1)).thenReturn(opinion);

        ResponseEntity<Opinion> response = opinionController.getOpinionById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(opinion, response.getBody());
        verify(opinionService, times(1)).getOpinionById(1);
    }

    @Test
    void testSaveOpinion() {
        Opinion opinion = new Opinion();
        when(opinionService.saveOpinion(opinion)).thenReturn(opinion);

        ResponseEntity<Opinion> response = opinionController.saveOpinion(opinion);

        assertEquals(OK, response.getStatusCode());
        assertEquals(opinion, response.getBody());
        verify(opinionService, times(1)).saveOpinion(opinion);
    }

    @Test
    void testUpdateOpinion_Success() {
        Opinion opinion = new Opinion();
        when(opinionService.updateOpinion(1, opinion)).thenReturn(opinion);

        ResponseEntity<Opinion> response = opinionController.updateOpinion(1, opinion);

        assertEquals(OK, response.getStatusCode());
        assertEquals(opinion, response.getBody());
        verify(opinionService, times(1)).updateOpinion(1, opinion);
    }

    @Test
    void testUpdateOpinion_NotFound() {
        Opinion opinion = new Opinion();
        when(opinionService.updateOpinion(1, opinion)).thenReturn(null);

        ResponseEntity<Opinion> response = opinionController.updateOpinion(1, opinion);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(opinionService, times(1)).updateOpinion(1, opinion);
    }

    @Test
    void testDeleteOpinionById() {
        doNothing().when(opinionService).deleteOpinionById(1);

        ResponseEntity<String> response = opinionController.deleteOpinionById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted opinion successfully", response.getBody());
        verify(opinionService, times(1)).deleteOpinionById(1);
    }
}
