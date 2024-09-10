package com.project.main.service;

import com.project.main.entity.UserPrompt;
import com.project.main.repository.UserPromptRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserPromptService {
    private final UserPromptRepo userPromptRepo;

    public UserPromptService(UserPromptRepo userPromptRepo) {
        this.userPromptRepo = userPromptRepo;
    }

    public List<UserPrompt> getAllUserPrompts() {
        return this.userPromptRepo.findAll();
    }

    public UserPrompt getUserPromptById(Integer promptId){
        Optional<UserPrompt> optionalUserPrompt = this.userPromptRepo.findById(promptId);
        if (optionalUserPrompt.isPresent()) {
            return optionalUserPrompt.get();
        }

        System.out.printf("User Prompt with id: $d doesn't exist", promptId);
        return null;
    }

    public UserPrompt saveUserPrompt(UserPrompt userPrompt){
        if (userPrompt.getDateTime() == null) {
            userPrompt.setDateTime(LocalDateTime.now());
        }
        UserPrompt savedUserPrompt = this.userPromptRepo.save(userPrompt);

        System.out.printf("User Prompt with id: %d saved successfully", savedUserPrompt.getId());
        return savedUserPrompt;
    }

    // public UserPrompt updateUserPrompt(UserPrompt userPrompt) {
    //     UserPrompt updatedUserPrompt = this.userPromptRepo.save(userPrompt);

    //     log.info("User Prompt with id: {} updated successfully", userPrompt.getId());
    //     return updatedUserPrompt;
    // }

    public void deleteUserPromptById(Integer id) {
        this.userPromptRepo.deleteById(id);
    }
}