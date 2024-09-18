package com.project.main.repository;

import com.project.main.entity.OpinionRelatedPapers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRelatedPaperRepo extends JpaRepository<OpinionRelatedPapers, Integer> {
    
}
