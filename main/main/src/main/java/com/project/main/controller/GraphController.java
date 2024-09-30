package com.project.main.controller;

import com.project.main.entity.Graph;
import com.project.main.entity.AppResponse;
import com.project.main.service.GraphService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/graphs")
public class GraphController {
    private final GraphService graphService;
    
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Graph>> getAllGraphs() {
        return ResponseEntity.ok().body(this.graphService.getAllGraphs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Graph> getGraphById(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.graphService.getGraphById(id));
    }

    @GetMapping("/{id}/app-response")
    public ResponseEntity<AppResponse> getAppResponseByGraph(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.graphService.getAppResponseByGraphId(id));
    }

    /**
     * Example of curl request for linking AppResponse to a Graph:
     * curl -s -X POST localhost:8080/graphs/ \
     * -H "Content-Type: application/json" \
     * -d '{"graphType": "CITATION", "appResponse": {"id": 1}}' | jq .
     */
    @PostMapping("/")
    public ResponseEntity<Graph> saveGraph(@RequestBody Graph graph)
    {
        return ResponseEntity.ok().body(this.graphService.saveGraph(graph));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Graph> updateGraph(@PathVariable Integer id, @RequestBody Graph updatedDetails) {
        Graph updatedGraph = this.graphService.updateGraph(id, updatedDetails);

        if (updatedGraph != null) {
            return ResponseEntity.ok().body(updatedGraph);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGraphById(@PathVariable Integer id)
    {
        this.graphService.deleteGraphById(id);
        return ResponseEntity.ok().body("Deleted graph successfully");
    }
}
