package com.project.main.repository;

import com.project.main.entity.ResearchPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResearchPaperRepo extends JpaRepository<ResearchPaper, Integer> {
}
