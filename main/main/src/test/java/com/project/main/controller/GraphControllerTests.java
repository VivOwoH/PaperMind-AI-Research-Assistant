package com.project.main.controller;

import com.project.main.entity.Graph;
import com.project.main.entity.AppResponse;
import com.project.main.service.GraphService;

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

class GraphControllerTests {

    @Mock
    private GraphService graphService;

    @InjectMocks
    private GraphController graphController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGraphs() {
        Graph graph1 = new Graph();
        Graph graph2 = new Graph();
        List<Graph> graphs = Arrays.asList(graph1, graph2);

        when(graphService.getAllGraphs()).thenReturn(graphs);

        ResponseEntity<List<Graph>> response = graphController.getAllGraphs();

        assertEquals(OK, response.getStatusCode());
        assertEquals(graphs, response.getBody());
        verify(graphService, times(1)).getAllGraphs();
    }

    @Test
    void testGetGraphById() {
        Graph graph = new Graph();
        when(graphService.getGraphById(1)).thenReturn(graph);

        ResponseEntity<Graph> response = graphController.getGraphById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(graph, response.getBody());
        verify(graphService, times(1)).getGraphById(1);
    }

    @Test
    void testGetAppResponseByGraph() {
        AppResponse appResponse = new AppResponse();
        when(graphService.getAppResponseByGraphId(1)).thenReturn(appResponse);

        ResponseEntity<AppResponse> response = graphController.getAppResponseByGraph(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(appResponse, response.getBody());
        verify(graphService, times(1)).getAppResponseByGraphId(1);
    }

    @Test
    void testSaveGraph() {
        Graph graph = new Graph();
        when(graphService.saveGraph(graph)).thenReturn(graph);

        ResponseEntity<Graph> response = graphController.saveGraph(graph);

        assertEquals(OK, response.getStatusCode());
        assertEquals(graph, response.getBody());
        verify(graphService, times(1)).saveGraph(graph);
    }

    @Test
    void testUpdateGraph_Success() {
        Graph graph = new Graph();
        when(graphService.updateGraph(1, graph)).thenReturn(graph);

        ResponseEntity<Graph> response = graphController.updateGraph(1, graph);

        assertEquals(OK, response.getStatusCode());
        assertEquals(graph, response.getBody());
        verify(graphService, times(1)).updateGraph(1, graph);
    }

    @Test
    void testUpdateGraph_NotFound() {
        Graph graph = new Graph();
        when(graphService.updateGraph(1, graph)).thenReturn(null);

        ResponseEntity<Graph> response = graphController.updateGraph(1, graph);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(graphService, times(1)).updateGraph(1, graph);
    }

    @Test
    void testDeleteGraphById() {
        doNothing().when(graphService).deleteGraphById(1);

        ResponseEntity<String> response = graphController.deleteGraphById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted graph successfully", response.getBody());
        verify(graphService, times(1)).deleteGraphById(1);
    }
}
