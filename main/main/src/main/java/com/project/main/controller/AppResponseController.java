package com.project.main.controller;

import com.project.main.entity.AppResponse;
import com.project.main.entity.UserPrompt;
import com.project.main.service.AppResponseService;
import com.project.main.repository.UserPromptRepo;

import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/app-responses")
public class AppResponseController {
    private final AppResponseService appResponseService;
    
    public AppResponseController(AppResponseService appResponseService) {
        this.appResponseService = appResponseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AppResponse>> getAllAppResponses() {
        return ResponseEntity.ok().body(this.appResponseService.getAllAppResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getAppResponseById(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.appResponseService.getAppResponseById(id));
    }

    @GetMapping("/{id}/user-prompt")
    public ResponseEntity<UserPrompt> getUserPromptByAppResponse(@PathVariable Integer id)
    {
        return ResponseEntity.ok().body(this.appResponseService.getUserPromptByAppResponseId(id));
    }

    /**
     * Example of curl request for linking UserPrompt to an AppResponse:
     * curl -s -X POST localhost:8080/app-responses/ \
     * -H "Content-Type: application/json" \
     * -d '{"processedPrompt": "foo", "generatedResponse": "bar", "userPrompt": {"id": 1}}' | jq .
     */
    @PostMapping("/")
    public ResponseEntity<AppResponse> saveAppResponse(@RequestBody AppResponse appResponse)
    {
        return ResponseEntity.ok().body(this.appResponseService.saveAppResponse(appResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppResponseById(@PathVariable Integer id)
    {
        this.appResponseService.deleteAppResponseById(id);
        return ResponseEntity.ok().body("Deleted app response successfully");
    }
}
