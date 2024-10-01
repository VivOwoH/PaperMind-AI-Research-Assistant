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

    // does not allow changing of id or datetime fields
    public UserPrompt updateUserPrompt(Integer id, UserPrompt newUserPromptDetails) {
        Optional<UserPrompt> optionalUserPrompt = this.userPromptRepo.findById(id);

        if (optionalUserPrompt.isPresent()) {
            UserPrompt existingUserPrompt = optionalUserPrompt.get();

            if (newUserPromptDetails.getId().equals(existingUserPrompt.getId())) {
                existingUserPrompt.setSearchPrompt(newUserPromptDetails.getSearchPrompt()); 
                existingUserPrompt.setSelectedFilter(newUserPromptDetails.getSelectedFilter()); 
                existingUserPrompt.setViewPreference(newUserPromptDetails.getViewPreference()); 
                existingUserPrompt.setGraphViewType(newUserPromptDetails.getGraphViewType()); 

                // Save the updated entity
                UserPrompt updatedUserPrompt = this.userPromptRepo.save(existingUserPrompt);

                System.out.printf("User Prompt with id: %d updated successfully\n", updatedUserPrompt.getId());
                return updatedUserPrompt;
            }
        }
        
        System.out.printf("User Prompt with id: %d doesn't exist\n", id);
        return null;
    }

    public void deleteUserPromptById(Integer id) {
        this.userPromptRepo.deleteById(id);
    }
}