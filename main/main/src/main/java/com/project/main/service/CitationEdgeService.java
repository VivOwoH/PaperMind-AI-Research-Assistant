package com.project.main.service;

import com.project.main.entity.CitationEdge;
import com.project.main.entity.ResearchPaper;
import com.project.main.repository.CitationEdgeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitationEdgeService {

    @Autowired
    private CitationEdgeRepo citationEdgeRepo;

    public CitationEdge saveCitationEdge(CitationEdge citationEdge) {
        return citationEdgeRepo.save(citationEdge);
    }

    public List<CitationEdge> getAllCitationEdges() {
        return citationEdgeRepo.findAll();
    }

    public CitationEdge getCitationEdgeById(Integer id) {
        return citationEdgeRepo.findById(id).orElse(null);
    }

    public void deleteCitationEdgeById(Integer id) {
        citationEdgeRepo.deleteById(id);
    }

    public List<CitationEdge> getCitationEdgesByFirstResearchPaper(ResearchPaper firstResearchPaper) {
        return citationEdgeRepo.findByFirstResearchPaper(firstResearchPaper);
    }

    public List<CitationEdge> getCitationEdgesBySecondResearchPaper(ResearchPaper secondResearchPaper) {
        return citationEdgeRepo.findBySecondResearchPaper(secondResearchPaper);
    }

    public List<CitationEdge> getCitationEdgesByFirstAndSecondResearchPapers(ResearchPaper firstResearchPaper, ResearchPaper secondResearchPaper) {
        return citationEdgeRepo.findByFirstResearchPaperAndSecondResearchPaper(firstResearchPaper, secondResearchPaper);
    }
}
