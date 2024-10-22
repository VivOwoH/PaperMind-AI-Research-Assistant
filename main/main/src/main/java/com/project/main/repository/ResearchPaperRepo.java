package com.project.main.repository;

import com.project.main.entity.ResearchPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResearchPaperRepo extends JpaRepository<ResearchPaper, Integer> {
    Optional<ResearchPaper> findBySemanticPaperId(String semanticPaperId);
}
