package com.project.main.controller;

import com.project.main.entity.ResearchPaper;
import com.project.main.service.ResearchPaperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/research-papers")
public class ResearchPaperController {

    private final ResearchPaperService researchPaperService;

    public ResearchPaperController(ResearchPaperService researchPaperService) {
        this.researchPaperService = researchPaperService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ResearchPaper>> getAllResearchPapers() {
        return ResponseEntity.ok().body(researchPaperService.getAllResearchPapers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearchPaper> getResearchPaperById(@PathVariable Integer id) {
        ResearchPaper paper = researchPaperService.getResearchPaperById(id);
        if (paper != null) {
            return ResponseEntity.ok().body(paper);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }


    @PostMapping("/")
    public ResponseEntity<ResearchPaper> saveResearchPaper(@RequestBody ResearchPaper researchPaper) {
        return ResponseEntity.ok().body(researchPaperService.saveResearchPaper(researchPaper));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResearchPaperById(@PathVariable Integer id) {
        researchPaperService.deleteResearchPaperById(id);
        return ResponseEntity.ok().body("Deleted research paper successfully");
    }
}
