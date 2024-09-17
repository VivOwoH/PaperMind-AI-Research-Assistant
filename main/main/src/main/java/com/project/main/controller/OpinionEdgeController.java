package com.project.main.controller;

import com.project.main.entity.OpinionEdge;
import com.project.main.entity.OpinionEdgeId;
import com.project.main.service.OpinionEdgeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/opinion-edges")
public class OpinionEdgeController {

    private final OpinionEdgeService opinionEdgeService;

    public OpinionEdgeController(OpinionEdgeService opinionEdgeService) {
        this.opinionEdgeService = opinionEdgeService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OpinionEdge>> getAllOpinionEdges() {
        return ResponseEntity.ok().body(this.opinionEdgeService.getAllOpinionEdges());
    }

    @GetMapping("/{graphId}/{opinionId}/{edgeId}")
    public ResponseEntity<OpinionEdge> getOpinionEdgeById(@PathVariable Integer graphId,
                                                          @PathVariable Integer opinionId,
                                                          @PathVariable Integer edgeId) {
        OpinionEdgeId opinionEdgeId = new OpinionEdgeId();
        opinionEdgeId.setGraphId(graphId);
        opinionEdgeId.setOpinionId(opinionId);
        opinionEdgeId.setEdgeId(edgeId);
        return ResponseEntity.ok().body(this.opinionEdgeService.getOpinionEdgeById(opinionEdgeId));
    }

    /**
     * Example of curl request for creating an OpinionEdge:
     * curl -s -X POST localhost:8080/opinion-edges/ \
     * -H "Content-Type: application/json" \
     * -d '{"graphId": 1, "opinionId": 1, "edgeId": 1}}' | jq .
     */
    @PostMapping("/")
    public ResponseEntity<OpinionEdge> saveOpinionEdge(@RequestBody OpinionEdge opinionEdge) {
        return ResponseEntity.ok().body(this.opinionEdgeService.saveOpinionEdge(opinionEdge));
    }

    @DeleteMapping("/{graphId}/{opinionId}/{edgeId}")
    public ResponseEntity<String> deleteOpinionEdgeById(@PathVariable Integer graphId,
                                                        @PathVariable Integer opinionId,
                                                        @PathVariable Integer edgeId) {
        OpinionEdgeId opinionEdgeId = new OpinionEdgeId();
        opinionEdgeId.setGraphId(graphId);
        opinionEdgeId.setOpinionId(opinionId);
        opinionEdgeId.setEdgeId(edgeId);
        this.opinionEdgeService.deleteOpinionEdgeById(opinionEdgeId);
        return ResponseEntity.ok().body("Deleted OpinionEdge successfully");
    }
}
