package com.project.main.service;

import com.project.main.entity.OpinionRelatedPapers;
import com.project.main.repository.OpinionRelatedPaperRepo;
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

class OpinionRelatedPaperServiceTests {

    @Mock
    private OpinionRelatedPaperRepo opinionRelatedPaperRepo;

    @InjectMocks
    private OpinionRelatedPaperService opinionRelatedPaperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOpinionRelatedPapers_Success() {
        OpinionRelatedPapers opinionRelatedPapers = new OpinionRelatedPapers();
        when(opinionRelatedPaperRepo.save(opinionRelatedPapers)).thenReturn(opinionRelatedPapers);

        OpinionRelatedPapers result = opinionRelatedPaperService.saveOpinionRelatedPapers(opinionRelatedPapers);

        assertNotNull(result);
        assertEquals(opinionRelatedPapers, result);
        verify(opinionRelatedPaperRepo, times(1)).save(opinionRelatedPapers);
    }

    @Test
    void testGetAllOpinionRelatedPapers_Success() {
        OpinionRelatedPapers paper1 = new OpinionRelatedPapers();
        OpinionRelatedPapers paper2 = new OpinionRelatedPapers();
        when(opinionRelatedPaperRepo.findAll()).thenReturn(Arrays.asList(paper1, paper2));

        List<OpinionRelatedPapers> papers = opinionRelatedPaperService.getAllOpinionRelatedPapers();

        assertEquals(2, papers.size());
        verify(opinionRelatedPaperRepo, times(1)).findAll();
    }

    @Test
    void testGetAllOpinionRelatedPapers_EmptyList() {
        when(opinionRelatedPaperRepo.findAll()).thenReturn(Collections.emptyList());

        List<OpinionRelatedPapers> papers = opinionRelatedPaperService.getAllOpinionRelatedPapers();

        assertTrue(papers.isEmpty());
        verify(opinionRelatedPaperRepo, times(1)).findAll();
    }

    @Test
    void testGetOpinionRelatedPapersById_Success() {
        OpinionRelatedPapers opinionRelatedPapers = new OpinionRelatedPapers();
        when(opinionRelatedPaperRepo.findById(1)).thenReturn(Optional.of(opinionRelatedPapers));

        OpinionRelatedPapers result = opinionRelatedPaperService.getOpinionRelatedPapersById(1);

        assertNotNull(result);
        assertEquals(opinionRelatedPapers, result);
        verify(opinionRelatedPaperRepo, times(1)).findById(1);
    }

    @Test
    void testGetOpinionRelatedPapersById_NotFound() {
        when(opinionRelatedPaperRepo.findById(1)).thenReturn(Optional.empty());

        OpinionRelatedPapers result = opinionRelatedPaperService.getOpinionRelatedPapersById(1);

        assertNull(result);
        verify(opinionRelatedPaperRepo, times(1)).findById(1);
    }

    @Test
    void testDeleteOpinionRelatedPapersById_Success() {
        doNothing().when(opinionRelatedPaperRepo).deleteById(1);

        opinionRelatedPaperService.deleteOpinionRelatedPapersById(1);

        verify(opinionRelatedPaperRepo, times(1)).deleteById(1);
    }
}
