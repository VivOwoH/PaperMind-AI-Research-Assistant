package com.project.main.service;

import com.project.main.entity.Opinion;
import com.project.main.repository.OpinionRepo;
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

class OpinionServiceTests {

    @Mock
    private OpinionRepo opinionRepo;

    @InjectMocks
    private OpinionService opinionService;

    private Opinion opinion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        opinion = new Opinion();
        opinion.setId(1);
        opinion.setOpinionVal("Sample Opinion");
    }

    @Test
    void testGetAllOpinions() {
        when(opinionRepo.findAll()).thenReturn(Arrays.asList(opinion));

        List<Opinion> result = opinionService.getAllOpinions();
        assertEquals(1, result.size());
        verify(opinionRepo, times(1)).findAll();
    }

    @Test
    void testGetOpinionById() {
        when(opinionRepo.findById(1)).thenReturn(Optional.of(opinion));

        Opinion result = opinionService.getOpinionById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(opinionRepo, times(1)).findById(1);
    }

    @Test
    void testGetOpinionByIdNotFound() {
        when(opinionRepo.findById(2)).thenReturn(Optional.empty());

        Opinion result = opinionService.getOpinionById(2);
        assertNull(result);
        verify(opinionRepo, times(1)).findById(2);
    }

    @Test
    void testSaveOpinionSuccess() {
        when(opinionRepo.save(opinion)).thenReturn(opinion);

        Opinion result = opinionService.saveOpinion(opinion);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(opinionRepo, times(1)).save(opinion);
    }

    @Test
    void testUpdateOpinionSuccess() {
        Opinion updatedOpinion = new Opinion();
        updatedOpinion.setId(1);
        updatedOpinion.setOpinionVal("Updated Opinion");

        when(opinionRepo.findById(1)).thenReturn(Optional.of(opinion));
        when(opinionRepo.save(opinion)).thenReturn(opinion);

        Opinion result = opinionService.updateOpinion(1, updatedOpinion);
        assertNotNull(result);
        assertEquals("Updated Opinion", result.getOpinionVal());
        verify(opinionRepo, times(1)).findById(1);
        verify(opinionRepo, times(1)).save(opinion);
    }

    @Test
    void testUpdateOpinionIdWrong() {
        Opinion updatedOpinion = new Opinion();
        updatedOpinion.setId(2); // Different ID from the existing opinion

        when(opinionRepo.findById(1)).thenReturn(Optional.of(opinion));

        Opinion result = opinionService.updateOpinion(1, updatedOpinion);
        assertNull(result);
        verify(opinionRepo, times(1)).findById(1);
        verify(opinionRepo, times(0)).save(any());
    }

    @Test
    void testUpdateOpinionNotFound() {
        Opinion updatedOpinion = new Opinion();
        updatedOpinion.setId(1);

        when(opinionRepo.findById(1)).thenReturn(Optional.empty());

        Opinion result = opinionService.updateOpinion(1, updatedOpinion);
        assertNull(result);
        verify(opinionRepo, times(1)).findById(1);
        verify(opinionRepo, times(0)).save(any());
    }

    @Test
    void testDeleteOpinionById() {
        opinionService.deleteOpinionById(1);

        verify(opinionRepo, times(1)).deleteById(1);
    }
}
