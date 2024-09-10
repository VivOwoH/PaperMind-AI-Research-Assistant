package com.project.main.repository;

import com.project.main.entity.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpinionRepo extends JpaRepository<Opinion, Integer> {
}