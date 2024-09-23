package com.project.main.service;

import com.project.main.entity.AppResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.repository.AppResponseRepo;
import com.project.main.repository.UserPromptRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppResponseService {
    private final AppResponseRepo appResponseRepo;
    private final UserPromptRepo userPromptRepo;

    public AppResponseService(AppResponseRepo appResponseRepo, UserPromptRepo userPromptRepo) {
        this.appResponseRepo = appResponseRepo;
        this.userPromptRepo = userPromptRepo;
    }

    // retrieves all AppResponse entities from the repo
    public List<AppResponse> getAllAppResponses() {
        return this.appResponseRepo.findAll();
    }

    // retrieves one AppResponse entity from the repo based on id
    public AppResponse getAppResponseById(Integer responseId) {
        Optional<AppResponse> optionalAppResponse = this.appResponseRepo.findById(responseId);
        if (optionalAppResponse.isPresent()) {
            return optionalAppResponse.get();
        }

        System.out.printf("App Response with id: %d doesn't exist", responseId);
        return null;
    }

    // retrieves one UserPrompt entity from the repo based on AppResponse entity
    public UserPrompt getUserPromptByAppResponseId(Integer responseId) {
        Optional<AppResponse> optionalAppResponse = this.appResponseRepo.findById(responseId);
        
        if (optionalAppResponse.isPresent()) {
            AppResponse appResponse = optionalAppResponse.get();
            return appResponse.getUserPrompt();
        }

        System.out.printf("App Response with id: %d doesn't exist", responseId);
        return null;
    }

    // saves an AppResponse entity to the repo
    public AppResponse saveAppResponse(AppResponse appResponse) {
        if (appResponse.getGeneratedDateTime() == null) {
            appResponse.setGeneratedDateTime(LocalDateTime.now());
        }

        // check if user prompt exists from the controller appResponse parameter
        if (appResponse.getUserPrompt() != null) {
            UserPrompt userPrompt = appResponse.getUserPrompt();

            // add user prompt entity to app response
            if (userPrompt.getId() != null) {
                Optional<UserPrompt> userPromptOptional = userPromptRepo.findById(userPrompt.getId());
                if (userPromptOptional.isPresent()) {
                    appResponse.setUserPrompt(userPromptOptional.get());
                } else {
                    throw new IllegalArgumentException("UserPrompt with the given ID does not exist");
                }
            }
        } else {
            // user prompt is not provided or doesn't have an ID
            throw new IllegalArgumentException("UserPrompt ID must be provided");
        }

        AppResponse savedAppResponse = this.appResponseRepo.save(appResponse);
        System.out.printf("App Response with id: %d saved successfully", savedAppResponse.getId());
        return savedAppResponse;
    }

    public AppResponse updateAppResponse(Integer id, AppResponse newAppResponseDetails) {
        Optional<AppResponse> optionalAppResponse = this.appResponseRepo.findById(id);

        if (optionalAppResponse.isPresent()) {
            AppResponse existingAppResponse = optionalAppResponse.get();

            if (newAppResponseDetails.getId().equals(existingAppResponse.getId())) {
                existingAppResponse.setUserPrompt(newAppResponseDetails.getUserPrompt()); 
                existingAppResponse.setGeneratedResponse(newAppResponseDetails.getGeneratedResponse()); 

                // Save the updated entity
                AppResponse updatedAppResponse = this.appResponseRepo.save(existingAppResponse);

                System.out.printf("App Response with id: %d updated successfully", updatedAppResponse.getId());
                return updatedAppResponse;
            }
        } 

        System.out.printf("App Response with id: %d doesn't exist", id);
        return null;
    }

    // deletes an AppResponse from the repo based on id
    public void deleteAppResponseById(Integer id) {
        this.appResponseRepo.deleteById(id);
    }
}