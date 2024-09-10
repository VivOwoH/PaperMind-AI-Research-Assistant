package com.project.main.repository;

import com.project.main.entity.UserPrompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPromptRepo extends JpaRepository<UserPrompt, Integer> {
}