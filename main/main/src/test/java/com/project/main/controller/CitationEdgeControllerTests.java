package com.project.main.controller;

import com.project.main.entity.CitationEdge;
import com.project.main.service.CitationEdgeService;
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

class CitationEdgeControllerTests {

    @Mock
    private CitationEdgeService citationEdgeService;

    @InjectMocks
    private CitationEdgeController citationEdgeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCitationEdges_Success() {
        CitationEdge citationEdge1 = new CitationEdge();
        CitationEdge citationEdge2 = new CitationEdge();
        List<CitationEdge> citationEdges = Arrays.asList(citationEdge1, citationEdge2);

        when(citationEdgeService.getAllCitationEdges()).thenReturn(citationEdges);

        List<CitationEdge> response = citationEdgeController.getAllCitationEdges();

        assertEquals(2, response.size());
        verify(citationEdgeService, times(1)).getAllCitationEdges();
    }

    @Test
    void testGetCitationEdgeById_Success() {
        CitationEdge citationEdge = new CitationEdge();
        when(citationEdgeService.getCitationEdgeById(1)).thenReturn(citationEdge);

        ResponseEntity<CitationEdge> response = citationEdgeController.getCitationEdgeById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(citationEdge, response.getBody());
        verify(citationEdgeService, times(1)).getCitationEdgeById(1);
    }

    @Test
    void testGetCitationEdgeById_NotFound() {
        when(citationEdgeService.getCitationEdgeById(1)).thenReturn(null);

        ResponseEntity<CitationEdge> response = citationEdgeController.getCitationEdgeById(1);

        assertEquals(404, response.getStatusCodeValue());
        verify(citationEdgeService, times(1)).getCitationEdgeById(1);
    }

    @Test
    void testCreateCitationEdge_Success() {
        CitationEdge citationEdge = new CitationEdge();
        when(citationEdgeService.saveCitationEdge(citationEdge)).thenReturn(citationEdge);

        CitationEdge response = citationEdgeController.createCitationEdge(citationEdge);

        assertNotNull(response);
        assertEquals(citationEdge, response);
        verify(citationEdgeService, times(1)).saveCitationEdge(citationEdge);
    }

    @Test
    void testDeleteCitationEdge_Success() {
        doNothing().when(citationEdgeService).deleteCitationEdgeById(1);

        ResponseEntity<Void> response = citationEdgeController.deleteCitationEdge(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(citationEdgeService, times(1)).deleteCitationEdgeById(1);
    }

    @Test
    void testDeleteCitationEdge_NotFound() {
        doThrow(new RuntimeException("Not found")).when(citationEdgeService).deleteCitationEdgeById(1);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            citationEdgeController.deleteCitationEdge(1);
        });

        assertEquals("Not found", exception.getMessage());
        verify(citationEdgeService, times(1)).deleteCitationEdgeById(1);
    }
}
