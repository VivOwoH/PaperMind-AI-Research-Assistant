package com.project.main.repository;

import com.project.main.entity.CitationEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitationEdgeRepo extends JpaRepository<CitationEdge, Integer> {
    
}
