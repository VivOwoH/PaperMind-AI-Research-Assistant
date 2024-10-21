package com.project.main.service;

import com.project.main.entity.ResearchPaper;
import com.project.main.repository.ResearchPaperRepo;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.List;
import java.util.Optional;

@Service
public class ResearchPaperService {

    private final ResearchPaperRepo researchPaperRepo;

    public ResearchPaperService(ResearchPaperRepo researchPaperRepo) {
        this.researchPaperRepo = researchPaperRepo;
    }

    public List<ResearchPaper> getAllResearchPapers() {
        return researchPaperRepo.findAll();
    }

    public ResearchPaper getResearchPaperById(Integer id) {
        Optional<ResearchPaper> researchPaper = researchPaperRepo.findById(id);
        return researchPaper.orElse(null);
    }

    public ResearchPaper getResearchPaperBySemanticPaperId(String semanticPaperId) {
        Optional<ResearchPaper> researchPaper = researchPaperRepo.findBySemanticPaperId(semanticPaperId);
        return researchPaper.orElse(null);
    }

    public ResearchPaper saveResearchPaper(ResearchPaper researchPaper) {
        return researchPaperRepo.save(researchPaper);
    }

    public ResearchPaper updateResearchPaper(Integer id, ResearchPaper newResearchPaperDetails) {
        Optional<ResearchPaper> optionalResearchPaper = this.researchPaperRepo.findById(id);

        if (optionalResearchPaper.isPresent()) {
            ResearchPaper existingResearchPaper = optionalResearchPaper.get();

            if (newResearchPaperDetails.getPaperId().equals(existingResearchPaper.getPaperId())) {
                existingResearchPaper.setTitle(newResearchPaperDetails.getTitle());
                existingResearchPaper.setPublishedDate(newResearchPaperDetails.getPublishedDate());
                existingResearchPaper.setAbstractText(newResearchPaperDetails.getAbstractText());
                existingResearchPaper.setMethodologySummary(newResearchPaperDetails.getMethodologySummary());
                existingResearchPaper.setSourceLink(newResearchPaperDetails.getSourceLink());
                existingResearchPaper.setCitationsCount(newResearchPaperDetails.getCitationsCount());
                existingResearchPaper.setViewsCount(newResearchPaperDetails.getViewsCount());
                existingResearchPaper.setAuthors(newResearchPaperDetails.getAuthors());

                // Save the updated entity
                ResearchPaper updatedResearchPaper = this.researchPaperRepo.save(existingResearchPaper);

                System.out.printf("ResearchPaper with id: %d updated successfully", updatedResearchPaper.getPaperId());
                return updatedResearchPaper;
            }
        }

        System.out.printf("ResearchPaper with id: %d doesn't exist", id);
        return null;
    }


    public void deleteResearchPaperById(Integer id) {
        researchPaperRepo.deleteById(id);
    }
}
