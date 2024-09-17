package com.project.main.service;

import com.project.main.entity.Opinion;
import com.project.main.repository.OpinionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {
    private final OpinionRepo opinionRepo;

    public OpinionService(OpinionRepo opinionRepo) {
        this.opinionRepo = opinionRepo;
    }

    public List<Opinion> getAllOpinions() {
        return this.opinionRepo.findAll();
    }

    public Opinion getOpinionById(Integer opinionId) {
        Optional<Opinion> optionalOpinion = this.opinionRepo.findById(opinionId);
        if (optionalOpinion.isPresent()) {
            return optionalOpinion.get();
        }

        System.out.printf("Opinion with id: %d doesn't exist", opinionId);
        return null;
    }

    public Opinion saveOpinion(Opinion opinion) {
        Opinion savedOpinion = this.opinionRepo.save(opinion);

        System.out.printf("Opinion with id: %d saved successfully", savedOpinion.getId());
        return savedOpinion;
    }

    public void deleteOpinionById(Integer id) {
        this.opinionRepo.deleteById(id);
    }
}
