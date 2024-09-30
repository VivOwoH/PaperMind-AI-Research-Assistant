package com.project.main.service;

import com.project.main.entity.Graph;
import com.project.main.entity.AppResponse;
import com.project.main.repository.AppResponseRepo;
import com.project.main.repository.GraphRepo;
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

class GraphServiceTests {

    @Mock
    private AppResponseRepo appResponseRepo;

    @Mock
    private GraphRepo graphRepo;

    @InjectMocks
    private GraphService graphService;

    private Graph graph;
    private AppResponse appResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        appResponse = new AppResponse();
        appResponse.setId(1);

        graph = new Graph();
        graph.setId(1);
        graph.setAppResponse(appResponse);
    }

    @Test
    void testGetAllGraphs() {
        when(graphRepo.findAll()).thenReturn(Arrays.asList(graph));

        List<Graph> result = graphService.getAllGraphs();
        assertEquals(1, result.size());
        verify(graphRepo, times(1)).findAll();
    }

    @Test
    void testGetGraphById() {
        when(graphRepo.findById(1)).thenReturn(Optional.of(graph));

        Graph result = graphService.getGraphById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(graphRepo, times(1)).findById(1);
    }

    @Test
    void testGetGraphByIdNotFound() {
        when(graphRepo.findById(2)).thenReturn(Optional.empty());

        Graph result = graphService.getGraphById(2);
        assertNull(result);
        verify(graphRepo, times(1)).findById(2);
    }

    @Test
    void testGetAppResponseByGraphId() {
        when(graphRepo.findById(1)).thenReturn(Optional.of(graph));

        AppResponse result = graphService.getAppResponseByGraphId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(graphRepo, times(1)).findById(1);
    }

    @Test
    void testGetAppResponseByGraphIdNotFound() {
        when(graphRepo.findById(2)).thenReturn(Optional.empty());

        AppResponse result = graphService.getAppResponseByGraphId(2);
        assertNull(result);
        verify(graphRepo, times(1)).findById(2);
    }

    @Test
    void testSaveGraph() {
        when(appResponseRepo.findById(1)).thenReturn(Optional.of(appResponse));
        when(graphRepo.save(graph)).thenReturn(graph);

        Graph result = graphService.saveGraph(graph);
        assertNotNull(result);
        assertEquals(1, result.getAppResponse().getId());
        verify(graphRepo, times(1)).save(graph);
    }

    @Test
    void testSaveGraphAppResponseNotFound() {
        when(appResponseRepo.findById(2)).thenReturn(Optional.empty());
        graph.getAppResponse().setId(2);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graphService.saveGraph(graph);
        });

        assertEquals("AppResponse with the given ID does not exist", exception.getMessage());
        verify(appResponseRepo, times(1)).findById(2);
        verify(graphRepo, times(0)).save(any());
    }

    @Test
    void testSaveGraphAppResponseNotProvided() {
        graph.setAppResponse(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graphService.saveGraph(graph);
        });

        assertEquals("AppResponse ID must be provided", exception.getMessage());
        verify(graphRepo, times(0)).save(any());
    }

    @Test
    void testUpdateGraphSuccess() {
        Graph updatedGraph = new Graph();
        updatedGraph.setId(1);
        updatedGraph.setAppResponse(appResponse);

        when(graphRepo.findById(1)).thenReturn(Optional.of(graph));
        when(graphRepo.save(graph)).thenReturn(graph);

        Graph result = graphService.updateGraph(1, updatedGraph);
        assertNotNull(result);
        verify(graphRepo, times(1)).findById(1);
        verify(graphRepo, times(1)).save(graph);
    }

    @Test
    void testUpdateGraphIdWrong() {
        Graph updatedGraph = new Graph();
        updatedGraph.setId(2);

        when(graphRepo.findById(1)).thenReturn(Optional.of(graph));

        Graph result = graphService.updateGraph(1, updatedGraph);
        assertNull(result);
        verify(graphRepo, times(1)).findById(1);
        verify(graphRepo, times(0)).save(any());
    }

    @Test
    void testUpdateGraphNotFound() {
        Graph updatedGraph = new Graph();
        updatedGraph.setId(1);

        when(graphRepo.findById(1)).thenReturn(Optional.empty());

        Graph result = graphService.updateGraph(1, updatedGraph);
        assertNull(result);
        verify(graphRepo, times(1)).findById(1);
        verify(graphRepo, times(0)).save(any());
    }

    @Test
    void testDeleteGraphById() {
        graphService.deleteGraphById(1);

        verify(graphRepo, times(1)).deleteById(1);
    }
}
