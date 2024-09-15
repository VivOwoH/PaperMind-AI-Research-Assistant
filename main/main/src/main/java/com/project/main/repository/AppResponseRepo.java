package com.project.main.repository;

import com.project.main.entity.AppResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppResponseRepo extends JpaRepository<AppResponse, Integer> {
}