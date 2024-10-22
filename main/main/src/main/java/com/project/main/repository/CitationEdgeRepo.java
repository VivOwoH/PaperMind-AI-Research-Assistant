package com.project.main.repository;

import com.project.main.entity.CitationEdge;
import com.project.main.entity.ResearchPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitationEdgeRepo extends JpaRepository<CitationEdge, Integer> {
    List<CitationEdge> findByFirstResearchPaper(ResearchPaper firstResearchPaper);
    
    List<CitationEdge> findBySecondResearchPaper(ResearchPaper secondResearchPaper);
    
    List<CitationEdge> findByFirstResearchPaperAndSecondResearchPaper(ResearchPaper firstResearchPaper, ResearchPaper secondResearchPaper);
}
