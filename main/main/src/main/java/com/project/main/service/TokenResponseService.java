package com.project.main.service;

import com.project.main.entity.TokenResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.repository.TokenResponseRepo;
import com.project.main.repository.UserPromptRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TokenResponseService {
    private final TokenResponseRepo tokenResponseRepo;
    private final UserPromptRepo userPromptRepo;

    public TokenResponseService(TokenResponseRepo tokenResponseRepo, UserPromptRepo userPromptRepo) {
        this.tokenResponseRepo = tokenResponseRepo;
        this.userPromptRepo = userPromptRepo;
    }

    // retrieves all TokenResponse entities from the repo
    public List<TokenResponse> getAllTokenResponses() {
        return this.tokenResponseRepo.findAll();
    }

    // retrieves one TokenResponse entity from the repo based on id
    public TokenResponse getTokenResponseById(Integer responseId){
        Optional<TokenResponse> optionalTokenResponse = this.tokenResponseRepo.findById(responseId);
        if (optionalTokenResponse.isPresent()) {
            return optionalTokenResponse.get();
        }

        System.out.printf("Token Response with id: $d doesn't exist", responseId);
        return null;
    }

    // retrieves one UserPrompt entity from the repo based on TokenResponse entity
    public UserPrompt getUserPromptByTokenResponseId(Integer responseId) {
        Optional<TokenResponse> optionalTokenResponse = this.tokenResponseRepo.findById(responseId);
        
        if (optionalTokenResponse.isPresent()) {
            TokenResponse tokenResponse = optionalTokenResponse.get();
            return tokenResponse.getUserPrompt();
        }

        System.out.printf("Token Response with id: %d doesn't exist", responseId);
        return null;
    }

    // saves a TokenResponse entity to the repo
    public TokenResponse saveTokenResponse(TokenResponse tokenResponse) {
        // check if user prompt exists from the controller tokenReponse parameter
        if (tokenResponse.getUserPrompt() != null) {
            UserPrompt userPrompt = tokenResponse.getUserPrompt();

            // add user prompt entity to token reponse
            if (userPrompt.getId() != null) {
                Optional<UserPrompt> userPromptOptional = userPromptRepo.findById(userPrompt.getId());
                if (userPromptOptional.isPresent()) {
                    tokenResponse.setUserPrompt(userPromptOptional.get());
                } else {
                    throw new IllegalArgumentException("UserPrompt with the given ID does not exist");
                }
            }
        } else {
            // user prompt is not provided or doesn't have an ID
            throw new IllegalArgumentException("UserPrompt ID must be provided");
        }

        TokenResponse savedTokenResponse = this.tokenResponseRepo.save(tokenResponse);
        System.out.printf("Token Response with id: %d saved successfully", savedTokenResponse.getId());
        return savedTokenResponse;
    }

    // does not allow changing of id field
    public TokenResponse updateTokenResponse(Integer id, TokenResponse newTokenResponseDetails) {
        Optional<TokenResponse> optionalTokenResponse = this.tokenResponseRepo.findById(id);

        if (optionalTokenResponse.isPresent()) {
            TokenResponse existingTokenResponse = optionalTokenResponse.get();

            if (newTokenResponseDetails.getId().equals(existingTokenResponse.getId())) {
                existingTokenResponse.setUserPrompt(newTokenResponseDetails.getUserPrompt()); 
                existingTokenResponse.setProcessedPrompt(newTokenResponseDetails.getProcessedPrompt()); 
                existingTokenResponse.setGeneratedResponse(newTokenResponseDetails.getGeneratedResponse()); 

                // Save the updated entity
                TokenResponse updatedTokenResponse = this.tokenResponseRepo.save(existingTokenResponse);

                System.out.printf("Token Response with id: %d updated successfully", updatedTokenResponse.getId());
                return updatedTokenResponse;
            }
        } 
        
        System.out.printf("Token Response with id: %d doesn't exist", id);
        return null;
    }

    // deletes a TokenResponse from the repo based on id
    public void deleteTokenResponseById(Integer id) {
        this.tokenResponseRepo.deleteById(id);
    }
}