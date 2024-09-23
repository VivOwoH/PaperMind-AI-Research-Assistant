package com.project.main.controller;

import com.project.main.entity.Opinion;
import com.project.main.service.OpinionService;

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
@RequestMapping("/opinions")
public class OpinionController {
    private final OpinionService opinionService;
    
    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Opinion>> getAllOpinions() {
        return ResponseEntity.ok().body(this.opinionService.getAllOpinions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opinion> getOpinionById(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.opinionService.getOpinionById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Opinion> saveOpinion(@RequestBody Opinion opinion)
    {
        return ResponseEntity.ok().body(this.opinionService.saveOpinion(opinion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Opinion> updateOpinion(@PathVariable Integer id, @RequestBody Opinion updatedDetails) {
        Opinion updatedOpinion = this.opinionService.updateOpinion(id, updatedDetails);

        if (updatedOpinion != null) {
            return ResponseEntity.ok().body(updatedOpinion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOpinionById(@PathVariable Integer id)
    {
        this.opinionService.deleteOpinionById(id);
        return ResponseEntity.ok().body("Deleted opinion successfully");
    }
}
