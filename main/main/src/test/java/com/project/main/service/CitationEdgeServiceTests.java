package com.project.main.service;

import com.project.main.entity.CitationEdge;
import com.project.main.entity.ResearchPaper;
import com.project.main.repository.CitationEdgeRepo;
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

class CitationEdgeServiceTests {

    @Mock
    private CitationEdgeRepo citationEdgeRepo;

    @InjectMocks
    private CitationEdgeService citationEdgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCitationEdge_Success() {
        CitationEdge citationEdge = new CitationEdge();
        when(citationEdgeRepo.save(citationEdge)).thenReturn(citationEdge);

        CitationEdge result = citationEdgeService.saveCitationEdge(citationEdge);

        assertNotNull(result);
        assertEquals(citationEdge, result);
        verify(citationEdgeRepo, times(1)).save(citationEdge);
    }

    @Test
    void testGetAllCitationEdges_Success() {
        CitationEdge edge1 = new CitationEdge();
        CitationEdge edge2 = new CitationEdge();
        when(citationEdgeRepo.findAll()).thenReturn(Arrays.asList(edge1, edge2));

        List<CitationEdge> edges = citationEdgeService.getAllCitationEdges();

        assertEquals(2, edges.size());
        verify(citationEdgeRepo, times(1)).findAll();
    }

    @Test
    void testGetAllCitationEdges_EmptyList() {
        when(citationEdgeRepo.findAll()).thenReturn(Collections.emptyList());

        List<CitationEdge> edges = citationEdgeService.getAllCitationEdges();

        assertTrue(edges.isEmpty());
        verify(citationEdgeRepo, times(1)).findAll();
    }

    @Test
    void testGetCitationEdgeById_Success() {
        CitationEdge citationEdge = new CitationEdge();
        when(citationEdgeRepo.findById(1)).thenReturn(Optional.of(citationEdge));

        CitationEdge result = citationEdgeService.getCitationEdgeById(1);

        assertNotNull(result);
        assertEquals(citationEdge, result);
        verify(citationEdgeRepo, times(1)).findById(1);
    }

    @Test
    void testGetCitationEdgeById_NotFound() {
        when(citationEdgeRepo.findById(1)).thenReturn(Optional.empty());

        CitationEdge result = citationEdgeService.getCitationEdgeById(1);

        assertNull(result);
        verify(citationEdgeRepo, times(1)).findById(1);
    }

    @Test
    void testDeleteCitationEdgeById_Success() {
        doNothing().when(citationEdgeRepo).deleteById(1);

        citationEdgeService.deleteCitationEdgeById(1);

        verify(citationEdgeRepo, times(1)).deleteById(1);
    }

    @Test
    void testGetCitationEdgesByFirstResearchPaperSuccess() {
        ResearchPaper researchPaper = new ResearchPaper();
        CitationEdge edge1 = new CitationEdge();
        CitationEdge edge2 = new CitationEdge();
        when(citationEdgeRepo.findByFirstResearchPaper(researchPaper)).thenReturn(Arrays.asList(edge1, edge2));

        List<CitationEdge> edges = citationEdgeService.getCitationEdgesByFirstResearchPaper(researchPaper);

        assertEquals(2, edges.size());
        verify(citationEdgeRepo, times(1)).findByFirstResearchPaper(researchPaper);
    }

    @Test
    void testGetCitationEdgesBySecondResearchPaperSuccess() {
        ResearchPaper researchPaper = new ResearchPaper();
        CitationEdge edge1 = new CitationEdge();
        CitationEdge edge2 = new CitationEdge();
        when(citationEdgeRepo.findBySecondResearchPaper(researchPaper)).thenReturn(Arrays.asList(edge1, edge2));

        List<CitationEdge> edges = citationEdgeService.getCitationEdgesBySecondResearchPaper(researchPaper);

        assertEquals(2, edges.size());
        verify(citationEdgeRepo, times(1)).findBySecondResearchPaper(researchPaper);
    }

    @Test
    void testGetCitationEdgesByFirstAndSecondResearchPapersSuccess() {
        ResearchPaper firstResearchPaper = new ResearchPaper();
        ResearchPaper secondResearchPaper = new ResearchPaper();
        CitationEdge edge = new CitationEdge();
        when(citationEdgeRepo.findByFirstResearchPaperAndSecondResearchPaper(firstResearchPaper, secondResearchPaper)).thenReturn(Arrays.asList(edge));

        List<CitationEdge> edges = citationEdgeService.getCitationEdgesByFirstAndSecondResearchPapers(firstResearchPaper, secondResearchPaper);

        assertEquals(1, edges.size());
        verify(citationEdgeRepo, times(1)).findByFirstResearchPaperAndSecondResearchPaper(firstResearchPaper, secondResearchPaper);
    }
}
