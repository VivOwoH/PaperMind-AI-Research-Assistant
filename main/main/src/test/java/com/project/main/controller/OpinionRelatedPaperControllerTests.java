package com.project.main.controller;

import com.project.main.entity.OpinionRelatedPapers;
import com.project.main.service.OpinionRelatedPaperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpinionRelatedPaperControllerTests {

    @Mock
    private OpinionRelatedPaperService opinionRelatedPaperService;

    @InjectMocks
    private OpinionRelatedPaperController opinionRelatedPaperController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOpinionRelatedPapers_Success() {
        OpinionRelatedPapers paper1 = new OpinionRelatedPapers();
        OpinionRelatedPapers paper2 = new OpinionRelatedPapers();
        List<OpinionRelatedPapers> papers = Arrays.asList(paper1, paper2);

        when(opinionRelatedPaperService.getAllOpinionRelatedPapers()).thenReturn(papers);

        List<OpinionRelatedPapers> response = opinionRelatedPaperController.getAllOpinionRelatedPapers();

        assertEquals(2, response.size());
        verify(opinionRelatedPaperService, times(1)).getAllOpinionRelatedPapers();
    }

    @Test
    void testGetOpinionRelatedPapersById_Success() {
        OpinionRelatedPapers paper = new OpinionRelatedPapers();
        when(opinionRelatedPaperService.getOpinionRelatedPapersById(1)).thenReturn(paper);

        ResponseEntity<OpinionRelatedPapers> response = opinionRelatedPaperController.getOpinionRelatedPapersById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(paper, response.getBody());
        verify(opinionRelatedPaperService, times(1)).getOpinionRelatedPapersById(1);
    }

    @Test
    void testGetOpinionRelatedPapersById_NotFound() {
        when(opinionRelatedPaperService.getOpinionRelatedPapersById(1)).thenReturn(null);

        ResponseEntity<OpinionRelatedPapers> response = opinionRelatedPaperController.getOpinionRelatedPapersById(1);

        assertEquals(404, response.getStatusCodeValue());
        verify(opinionRelatedPaperService, times(1)).getOpinionRelatedPapersById(1);
    }

    @Test
    void testCreateOpinionRelatedPapers_Success() {
        OpinionRelatedPapers paper = new OpinionRelatedPapers();
        when(opinionRelatedPaperService.saveOpinionRelatedPapers(paper)).thenReturn(paper);

        OpinionRelatedPapers response = opinionRelatedPaperController.createOpinionRelatedPapers(paper);

        assertNotNull(response);
        assertEquals(paper, response);
        verify(opinionRelatedPaperService, times(1)).saveOpinionRelatedPapers(paper);
    }

    @Test
    void testDeleteOpinionRelatedPapers_Success() {
        doNothing().when(opinionRelatedPaperService).deleteOpinionRelatedPapersById(1);

        ResponseEntity<Void> response = opinionRelatedPaperController.deleteOpinionRelatedPapers(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(opinionRelatedPaperService, times(1)).deleteOpinionRelatedPapersById(1);
    }

    @Test
    void testDeleteOpinionRelatedPapers_NotFound() {
        doThrow(new RuntimeException("Not found")).when(opinionRelatedPaperService).deleteOpinionRelatedPapersById(1);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            opinionRelatedPaperController.deleteOpinionRelatedPapers(1);
        });

        assertEquals("Not found", exception.getMessage());
        verify(opinionRelatedPaperService, times(1)).deleteOpinionRelatedPapersById(1);
    }
}
