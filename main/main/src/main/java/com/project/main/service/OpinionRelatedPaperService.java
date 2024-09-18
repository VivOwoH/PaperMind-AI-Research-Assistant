package com.project.main.service;

import com.project.main.entity.OpinionRelatedPapers;
import com.project.main.repository.OpinionRelatedPaperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpinionRelatedPaperService {

    @Autowired
    private OpinionRelatedPaperRepo opinionRelatedPaperRepo;

    public OpinionRelatedPapers saveOpinionRelatedPapers(OpinionRelatedPapers opinionRelatedPapers) {
        return opinionRelatedPaperRepo.save(opinionRelatedPapers);
    }

    public List<OpinionRelatedPapers> getAllOpinionRelatedPapers() {
        return opinionRelatedPaperRepo.findAll();
    }

    public OpinionRelatedPapers getOpinionRelatedPapersById(Integer id) {
        return opinionRelatedPaperRepo.findById(id).orElse(null);
    }

    public void deleteOpinionRelatedPapersById(Integer id) {
        opinionRelatedPaperRepo.deleteById(id);
    }
}
