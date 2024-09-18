package com.project.main.controller;

import com.project.main.entity.OpinionRelatedPapers;
import com.project.main.service.OpinionRelatedPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opinion-related-papers")
public class OpinionRelatedPaperController {

    @Autowired
    private OpinionRelatedPaperService opinionRelatedPapersService;

    // Get all OpinionRelatedPapers
    @GetMapping
    public List<OpinionRelatedPapers> getAllOpinionRelatedPapers() {
        return opinionRelatedPapersService.getAllOpinionRelatedPapers();
    }

    // Get an OpinionRelatedPapers by ID
    @GetMapping("/{id}")
    public ResponseEntity<OpinionRelatedPapers> getOpinionRelatedPapersById(@PathVariable Integer id) {
        OpinionRelatedPapers opinionRelatedPapers = opinionRelatedPapersService.getOpinionRelatedPapersById(id);
        if (opinionRelatedPapers != null) {
            return ResponseEntity.ok(opinionRelatedPapers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new OpinionRelatedPapers
    @PostMapping
    public OpinionRelatedPapers createOpinionRelatedPapers(@RequestBody OpinionRelatedPapers opinionRelatedPapers) {
        return opinionRelatedPapersService.saveOpinionRelatedPapers(opinionRelatedPapers);
    }

    // Delete an OpinionRelatedPapers by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpinionRelatedPapers(@PathVariable Integer id) {
        opinionRelatedPapersService.deleteOpinionRelatedPapersById(id);
        return ResponseEntity.noContent().build();
    }
}
