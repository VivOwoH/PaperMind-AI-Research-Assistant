package com.project.main.repository;

import com.project.main.entity.Graph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GraphRepo extends JpaRepository<Graph, Integer> {
}