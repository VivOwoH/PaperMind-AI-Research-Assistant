package com.project.main.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
public class TokenResponse {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer responseId;

    @OneToOne
    @JoinColumn(name="prompt_id", referencedColumnName = "id", nullable=false)
    private UserPrompt userPrompt;

    @Column(name="processed_prompt")
    private String processedPrompt;

    @Column(name="generated_response", length = 1024)
    private String generatedResponse;

    public TokenResponse() {}

    protected TokenResponse(UserPrompt userPrompt, String generatedResponse, String processedPrompt) {
        this.userPrompt = userPrompt;
        this.generatedResponse = generatedResponse;
        this.processedPrompt = processedPrompt;
    }

    public Integer getId() {
        return this.responseId;
    }

    public void setId(Integer id) {
        this.responseId = id;
    }

    public UserPrompt getUserPrompt() {
        return this.userPrompt;
    }

    public void setUserPrompt(UserPrompt userPrompt) {
        this.userPrompt = userPrompt;
    }

    public String getProcessedPrompt() {
        return this.processedPrompt;
    }

    public void setProcessedPrompt(String processedPrompt) {
        this.processedPrompt = processedPrompt;
    }

    public String getGeneratedResponse() {
        return this.generatedResponse;
    }

    public void setGeneratedResponse(String generatedResponse) {
        this.generatedResponse = generatedResponse;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "responseId=" + responseId +
                ", userPrompt=" + userPrompt.toString() +
                ", processedPrompt='" + processedPrompt + '\'' +
                ", generatedResponse='" + generatedResponse + '\'' +
                '}';
    }
}