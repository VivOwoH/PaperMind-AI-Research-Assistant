package com.project.main.controller;

import com.project.main.entity.OpinionEdge;
import com.project.main.entity.OpinionEdgeId;
import com.project.main.service.OpinionEdgeService;

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

class OpinionEdgeControllerTests {

    @Mock
    private OpinionEdgeService opinionEdgeService;

    @InjectMocks
    private OpinionEdgeController opinionEdgeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOpinionEdges() {
        OpinionEdge edge1 = new OpinionEdge();
        OpinionEdge edge2 = new OpinionEdge();
        List<OpinionEdge> edges = Arrays.asList(edge1, edge2);

        when(opinionEdgeService.getAllOpinionEdges()).thenReturn(edges);

        ResponseEntity<List<OpinionEdge>> response = opinionEdgeController.getAllOpinionEdges();

        assertEquals(OK, response.getStatusCode());
        assertEquals(edges, response.getBody());
        verify(opinionEdgeService, times(1)).getAllOpinionEdges();
    }

    @Test
    void testGetOpinionEdgeById() {
        OpinionEdge edge = new OpinionEdge();
        OpinionEdgeId edgeId = new OpinionEdgeId();
        edgeId.setGraphId(1);
        edgeId.setOpinionId(1);
        edgeId.setEdgeId(1);
        
        when(opinionEdgeService.getOpinionEdgeById(edgeId)).thenReturn(edge);

        ResponseEntity<OpinionEdge> response = opinionEdgeController.getOpinionEdgeById(1, 1, 1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(edge, response.getBody());
        verify(opinionEdgeService, times(1)).getOpinionEdgeById(edgeId);
    }

    @Test
    void testSaveOpinionEdge() {
        OpinionEdge edge = new OpinionEdge();
        when(opinionEdgeService.saveOpinionEdge(edge)).thenReturn(edge);

        ResponseEntity<OpinionEdge> response = opinionEdgeController.saveOpinionEdge(edge);
        assertEquals(OK, response.getStatusCode());
        assertEquals(edge, response.getBody());
        verify(opinionEdgeService, times(1)).saveOpinionEdge(edge);
    }

    @Test
    void testDeleteOpinionEdgeById() {
        OpinionEdgeId edgeId = new OpinionEdgeId();
        edgeId.setGraphId(1);
        edgeId.setOpinionId(1);
        edgeId.setEdgeId(1);
        
        doNothing().when(opinionEdgeService).deleteOpinionEdgeById(edgeId);

        ResponseEntity<String> response = opinionEdgeController.deleteOpinionEdgeById(1, 1, 1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted OpinionEdge successfully", response.getBody());
        verify(opinionEdgeService, times(1)).deleteOpinionEdgeById(edgeId);
    }
}
