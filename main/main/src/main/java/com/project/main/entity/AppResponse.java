package com.project.main.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

@Entity
public class AppResponse {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer responseId;

    @OneToOne
    @JoinColumn(name="prompt_id", referencedColumnName = "id", nullable=false)
    private UserPrompt userPrompt;

    @Lob
    @Column(name="generated_response")
    private String generatedResponse;

    @Column(name="generated_date_time")
    private LocalDateTime generatedDateTime;

    @OneToOne(mappedBy="appResponse", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private Graph graph;

    public AppResponse() {}

    protected AppResponse(UserPrompt userPrompt, String generatedResponse) {
        this.userPrompt = userPrompt;
        this.generatedResponse = generatedResponse;
        this.generatedDateTime = null;
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

    public String getGeneratedResponse() {
        return this.generatedResponse;
    }

    public void setGeneratedResponse(String generatedResponse) {
        this.generatedResponse = generatedResponse;
    }

    public LocalDateTime getGeneratedDateTime() {
        return this.generatedDateTime;
    }

    public void setGeneratedDateTime(LocalDateTime generatedDateTime) {
        this.generatedDateTime = generatedDateTime;
    }

    @Override
    public String toString() {
        return "AppResponse{" +
                "responseId=" + responseId +
                ", userPrompt=" + userPrompt.toString() +
                ", generatedResponse='" + generatedResponse + '\'' +
                ", generatedDateTime='" + generatedDateTime + '\'' +
                '}';
    }
}