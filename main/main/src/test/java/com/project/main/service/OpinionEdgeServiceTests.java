package com.project.main.service;

import com.project.main.entity.OpinionEdge;
import com.project.main.entity.OpinionEdgeId;
import com.project.main.repository.OpinionEdgeRepo;
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

class OpinionEdgeServiceTest {

    @Mock
    private OpinionEdgeRepo opinionEdgeRepo;

    @InjectMocks
    private OpinionEdgeService opinionEdgeService;

    private OpinionEdge opinionEdge;
    private OpinionEdgeId opinionEdgeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        opinionEdgeId = new OpinionEdgeId();
        opinionEdgeId.setEdgeId(1);
        opinionEdgeId.setGraphId(1);
        opinionEdgeId.setOpinionId(1);

        opinionEdge = new OpinionEdge();
        opinionEdge.setId(opinionEdgeId);
    }

    @Test
    void testGetAllOpinionEdges() {
        when(opinionEdgeRepo.findAll()).thenReturn(Arrays.asList(opinionEdge));

        List<OpinionEdge> result = opinionEdgeService.getAllOpinionEdges();
        assertEquals(1, result.size());
        verify(opinionEdgeRepo, times(1)).findAll();
    }

    @Test
    void testGetOpinionEdgeByIdSuccess() {
        when(opinionEdgeRepo.findById(opinionEdgeId)).thenReturn(Optional.of(opinionEdge));

        OpinionEdge result = opinionEdgeService.getOpinionEdgeById(opinionEdgeId);
        assertNotNull(result);
        assertEquals(1, result.getId().getEdgeId());
        verify(opinionEdgeRepo, times(1)).findById(opinionEdgeId);
    }

    @Test
    void testGetOpinionEdgeByIdNotFound() {
        when(opinionEdgeRepo.findById(opinionEdgeId)).thenReturn(Optional.empty());

        OpinionEdge result = opinionEdgeService.getOpinionEdgeById(opinionEdgeId);
        assertNull(result);
        verify(opinionEdgeRepo, times(1)).findById(opinionEdgeId);
    }

    @Test
    void testSaveOpinionEdgeSuccess() {
        when(opinionEdgeRepo.save(opinionEdge)).thenReturn(opinionEdge);

        OpinionEdge result = opinionEdgeService.saveOpinionEdge(opinionEdge);
        assertNotNull(result);
        assertEquals(1, result.getId().getEdgeId());
        verify(opinionEdgeRepo, times(1)).save(opinionEdge);
    }

    @Test
    void testSaveOpinionEdgeNullId() {
        OpinionEdge newOpinionEdge = new OpinionEdge();
        when(opinionEdgeRepo.save(any(OpinionEdge.class))).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            opinionEdgeService.saveOpinionEdge(newOpinionEdge);
        });
        assertEquals("OpinionEdge ID must be provided", exception.getMessage());
        verify(opinionEdgeRepo, times(0)).save(newOpinionEdge);
    }

    @Test
    void testDeleteOpinionEdgeById() {
        opinionEdgeService.deleteOpinionEdgeById(opinionEdgeId);

        verify(opinionEdgeRepo, times(1)).deleteById(opinionEdgeId);
    }
}