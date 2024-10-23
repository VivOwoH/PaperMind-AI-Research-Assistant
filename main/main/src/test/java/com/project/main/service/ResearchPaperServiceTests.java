package com.project.main.service;

import com.project.main.entity.ResearchPaper;
import com.project.main.repository.ResearchPaperRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResearchPaperServiceTests {

    @Mock
    private ResearchPaperRepo researchPaperRepo;

    @InjectMocks
    private ResearchPaperService researchPaperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllResearchPapers_Success() {
        ResearchPaper paper1 = new ResearchPaper();
        ResearchPaper paper2 = new ResearchPaper();
        when(researchPaperRepo.findAll()).thenReturn(Arrays.asList(paper1, paper2));

        List<ResearchPaper> papers = researchPaperService.getAllResearchPapers();

        assertEquals(2, papers.size());
        verify(researchPaperRepo, times(1)).findAll();
    }

    @Test
    void testGetAllResearchPapers_EmptyList() {
        when(researchPaperRepo.findAll()).thenReturn(Collections.emptyList());

        List<ResearchPaper> papers = researchPaperService.getAllResearchPapers();

        assertTrue(papers.isEmpty());
        verify(researchPaperRepo, times(1)).findAll();
    }

    @Test
    void testGetResearchPaperById_Success() {
        ResearchPaper researchPaper = new ResearchPaper();
        when(researchPaperRepo.findById(1)).thenReturn(Optional.of(researchPaper));

        ResearchPaper result = researchPaperService.getResearchPaperById(1);

        assertNotNull(result);
        assertEquals(researchPaper, result);
        verify(researchPaperRepo, times(1)).findById(1);
    }

    @Test
    void testGetResearchPaperById_NotFound() {
        when(researchPaperRepo.findById(1)).thenReturn(Optional.empty());

        ResearchPaper result = researchPaperService.getResearchPaperById(1);

        assertNull(result);
        verify(researchPaperRepo, times(1)).findById(1);
    }

    @Test
    void testSaveResearchPaper_Success() {
        ResearchPaper researchPaper = new ResearchPaper();
        when(researchPaperRepo.save(researchPaper)).thenReturn(researchPaper);

        ResearchPaper result = researchPaperService.saveResearchPaper(researchPaper);

        assertNotNull(result);
        assertEquals(researchPaper, result);
        verify(researchPaperRepo, times(1)).save(researchPaper);
    }

    @Test
    void testDeleteResearchPaperById_Success() {
        doNothing().when(researchPaperRepo).deleteById(1);

        researchPaperService.deleteResearchPaperById(1);

        verify(researchPaperRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetResearchPaperBySemanticPaperIdSuccess() {
        ResearchPaper researchPaper = new ResearchPaper();
        when(researchPaperRepo.findBySemanticPaperId("SP123")).thenReturn(Optional.of(researchPaper));

        ResearchPaper result = researchPaperService.getResearchPaperBySemanticPaperId("SP123");

        assertNotNull(result);
        assertEquals(researchPaper, result);
        verify(researchPaperRepo, times(1)).findBySemanticPaperId("SP123");
    }

    @Test
    void testUpdateResearchPaperPaperIdMismatch() {
        ResearchPaper existingResearchPaper = new ResearchPaper();
        existingResearchPaper.setPaperId(1);
        ResearchPaper newResearchPaperDetails = new ResearchPaper();
        newResearchPaperDetails.setPaperId(2);

        when(researchPaperRepo.findById(1)).thenReturn(Optional.of(existingResearchPaper));

        ResearchPaper result = researchPaperService.updateResearchPaper(1, newResearchPaperDetails);

        assertNull(result);
        verify(researchPaperRepo, times(1)).findById(1);
        verify(researchPaperRepo, times(0)).save(any(ResearchPaper.class));
    }

    @Test
    void testGetResearchPaperBySemanticPaperIdNotFound() {
        when(researchPaperRepo.findBySemanticPaperId("SP123")).thenReturn(Optional.empty());

        ResearchPaper result = researchPaperService.getResearchPaperBySemanticPaperId("SP123");

        assertNull(result);
        verify(researchPaperRepo, times(1)).findBySemanticPaperId("SP123");
    }

    @Test
    void testUpdateResearchPaperSuccess() {
        ResearchPaper existingResearchPaper = new ResearchPaper();
        existingResearchPaper.setPaperId(1);
        ResearchPaper newResearchPaperDetails = new ResearchPaper();
        newResearchPaperDetails.setPaperId(1);
        newResearchPaperDetails.setTitle("Updated Title");

        when(researchPaperRepo.findById(1)).thenReturn(Optional.of(existingResearchPaper));
        when(researchPaperRepo.save(existingResearchPaper)).thenReturn(existingResearchPaper);

        ResearchPaper result = researchPaperService.updateResearchPaper(1, newResearchPaperDetails);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(researchPaperRepo, times(1)).findById(1);
        verify(researchPaperRepo, times(1)).save(existingResearchPaper);
    }

    @Test
    void testUpdateResearchPaperNotFound() {
        ResearchPaper newResearchPaperDetails = new ResearchPaper();
        newResearchPaperDetails.setPaperId(1);

        when(researchPaperRepo.findById(1)).thenReturn(Optional.empty());

        ResearchPaper result = researchPaperService.updateResearchPaper(1, newResearchPaperDetails);

        assertNull(result);
        verify(researchPaperRepo, times(1)).findById(1);
        verify(researchPaperRepo, times(0)).save(any(ResearchPaper.class));
    }
}
