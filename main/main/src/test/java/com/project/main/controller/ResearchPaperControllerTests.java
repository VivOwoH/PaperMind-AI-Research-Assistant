package com.project.main.controller;

import com.project.main.entity.ResearchPaper;
import com.project.main.service.ResearchPaperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class ResearchPaperControllerTests {

    @Mock
    private ResearchPaperService researchPaperService;

    @InjectMocks
    private ResearchPaperController researchPaperController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllResearchPapers() {
        ResearchPaper paper1 = new ResearchPaper("1234abcd", "Title 1", LocalDateTime.now(), "Abstract 1", "Methodology 1", "http://source1.com", 10, 100);
        ResearchPaper paper2 = new ResearchPaper("5678efgh", "Title 2", LocalDateTime.now(), "Abstract 2", "Methodology 2", "http://source2.com", 5, 50);
        List<ResearchPaper> researchPapers = Arrays.asList(paper1, paper2);

        when(researchPaperService.getAllResearchPapers()).thenReturn(researchPapers);

        ResponseEntity<List<ResearchPaper>> response = researchPaperController.getAllResearchPapers();

        assertEquals(OK, response.getStatusCode());
        assertEquals(researchPapers, response.getBody());
        verify(researchPaperService, times(1)).getAllResearchPapers();
    }

    @Test
    void testGetResearchPaperById_Success() {
        ResearchPaper researchPaper = new ResearchPaper("1234abcd", "Title 1", LocalDateTime.now(), "Abstract 1", "Methodology 1", "http://source1.com", 10, 100);
        when(researchPaperService.getResearchPaperById(1)).thenReturn(researchPaper);

        ResponseEntity<ResearchPaper> response = researchPaperController.getResearchPaperById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(researchPaper, response.getBody());
        verify(researchPaperService, times(1)).getResearchPaperById(1);
    }

    @Test
    void testGetResearchPaperById_NotFound() {
        when(researchPaperService.getResearchPaperById(1)).thenReturn(null);

        ResponseEntity<ResearchPaper> response = researchPaperController.getResearchPaperById(1);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(researchPaperService, times(1)).getResearchPaperById(1);
    }

    @Test
    void testSaveResearchPaper() {
        ResearchPaper researchPaper = new ResearchPaper("1234abcd", "Title 1", LocalDateTime.now(), "Abstract 1", "Methodology 1", "http://source1.com", 10, 100);
        when(researchPaperService.saveResearchPaper(researchPaper)).thenReturn(researchPaper);

        ResponseEntity<ResearchPaper> response = researchPaperController.saveResearchPaper(researchPaper);

        assertEquals(OK, response.getStatusCode());
        assertEquals(researchPaper, response.getBody());
        verify(researchPaperService, times(1)).saveResearchPaper(researchPaper);
    }

    @Test
    void testDeleteResearchPaperById() {
        doNothing().when(researchPaperService).deleteResearchPaperById(1);

        ResponseEntity<String> response = researchPaperController.deleteResearchPaperById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted research paper successfully", response.getBody());
        verify(researchPaperService, times(1)).deleteResearchPaperById(1);
    }
}
