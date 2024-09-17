package com.project.main.repository;

import com.project.main.entity.OpinionEdge;
import com.project.main.entity.OpinionEdgeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpinionEdgeRepo extends JpaRepository<OpinionEdge, OpinionEdgeId> {
}