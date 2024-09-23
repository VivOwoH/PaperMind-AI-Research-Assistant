package com.project.main.service;

import com.project.main.entity.OpinionEdge;
import com.project.main.entity.OpinionEdgeId;
import com.project.main.repository.OpinionEdgeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpinionEdgeService {

    private final OpinionEdgeRepo opinionEdgeRepo;

    public OpinionEdgeService(OpinionEdgeRepo opinionEdgeRepo) {
        this.opinionEdgeRepo = opinionEdgeRepo;
    }

    public List<OpinionEdge> getAllOpinionEdges() {
        return this.opinionEdgeRepo.findAll();
    }

    public OpinionEdge getOpinionEdgeById(OpinionEdgeId opinionEdgeId){
        Optional<OpinionEdge> optionalOpinionEdge = this.opinionEdgeRepo.findById(opinionEdgeId);
        if (optionalOpinionEdge.isPresent()) {
            return optionalOpinionEdge.get();
        }

        System.out.printf("Opinion Edge with id: %s doesn't exist", opinionEdgeId.toString());
        return null;
    }

    public OpinionEdge saveOpinionEdge(OpinionEdge opinionEdge){
        if (opinionEdge.getId() == null) {
            throw new IllegalArgumentException("OpinionEdge ID must be provided");
        }

        OpinionEdge savedOpinionEdge = this.opinionEdgeRepo.save(opinionEdge);
        System.out.printf("OpinionEdge with id: %d %d %d saved successfully\n", 
            savedOpinionEdge.getId().getEdgeId(), savedOpinionEdge.getId().getGraphId(), savedOpinionEdge.getId().getOpinionId());
        return savedOpinionEdge;
    }

    public void deleteOpinionEdgeById(OpinionEdgeId opinionEdgeId) {
        this.opinionEdgeRepo.deleteById(opinionEdgeId);
    }
}
