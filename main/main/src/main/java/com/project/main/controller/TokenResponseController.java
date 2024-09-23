package com.project.main.controller;

import com.project.main.entity.TokenResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.service.TokenResponseService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/token-responses")
public class TokenResponseController {
    private final TokenResponseService tokenResponseService;
    
    public TokenResponseController(TokenResponseService tokenResponseService) {
        this.tokenResponseService = tokenResponseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<TokenResponse>> getAllTokenResponses() {
        return ResponseEntity.ok().body(this.tokenResponseService.getAllTokenResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenResponse> getTokenResponseById(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.tokenResponseService.getTokenResponseById(id));
    }

    @GetMapping("/{id}/user-prompt")
    public ResponseEntity<UserPrompt> getUserPromptByTokenResponse(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.tokenResponseService.getUserPromptByTokenResponseId(id));
    }

    /**
     * Example of curl request for linking UserPrompt to a TokenResponse:
     * curl -s -X POST localhost:8080/token-responses/ \
     * -H "Content-Type: application/json" \
     * -d '{"processedPrompt": "foo", "generatedResponse": "bar", "userPrompt": {"id": 1}}' | jq .
     */
    @PostMapping("/")
    public ResponseEntity<TokenResponse> saveTokenResponse(@RequestBody TokenResponse tokenResponse)
    {
        return ResponseEntity.ok().body(this.tokenResponseService.saveTokenResponse(tokenResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TokenResponse> updateTokenResponse(@PathVariable Integer id, @RequestBody TokenResponse updatedDetails) {
        TokenResponse updatedTokenResponse = this.tokenResponseService.updateTokenResponse(id, updatedDetails);

        if (updatedTokenResponse != null) {
            return ResponseEntity.ok().body(updatedTokenResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTokenResponseById(@PathVariable Integer id)
    {
        this.tokenResponseService.deleteTokenResponseById(id);
        return ResponseEntity.ok().body("Deleted token response successfully");
    }
}