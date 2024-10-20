package com.project.main.service;

import com.project.main.entity.ResearchPaper;
import com.project.main.repository.ResearchPaperRepo;
import org.springframework.stereotype.Service;

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

    public ResearchPaper saveResearchPaper(ResearchPaper researchPaper) {
        return researchPaperRepo.save(researchPaper);
    }

    public void deleteResearchPaperById(Integer id) {
        researchPaperRepo.deleteById(id);
    }
}
