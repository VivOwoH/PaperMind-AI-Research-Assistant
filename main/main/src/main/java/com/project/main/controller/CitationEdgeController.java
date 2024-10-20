package com.project.main.controller;

import com.project.main.entity.CitationEdge;
import com.project.main.service.CitationEdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citation-edges")
public class CitationEdgeController {

    @Autowired
    private CitationEdgeService citationEdgeService;

    // Get all CitationEdges
    @GetMapping
    public List<CitationEdge> getAllCitationEdges() {
        return citationEdgeService.getAllCitationEdges();
    }

    // Get a CitationEdge by ID
    @GetMapping("/{id}")
    public ResponseEntity<CitationEdge> getCitationEdgeById(@PathVariable Integer id) {
        CitationEdge citationEdge = citationEdgeService.getCitationEdgeById(id);
        if (citationEdge != null) {
            return ResponseEntity.ok(citationEdge);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new CitationEdge
    @PostMapping
    public CitationEdge createCitationEdge(@RequestBody CitationEdge citationEdge) {
        return citationEdgeService.saveCitationEdge(citationEdge);
    }

    // Delete a CitationEdge by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCitationEdge(@PathVariable Integer id) {
        citationEdgeService.deleteCitationEdgeById(id);
        return ResponseEntity.noContent().build();
    }
}
