package com.project.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class UserPrompt {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer promptId;

    private String searchPrompt;
    private LocalDateTime dateTime;
    private String selectedFilter;
    private String viewPreference;
    private String graphViewType;

    private UserPrompt() {}

    protected UserPrompt(String searchPrompt, String selectedFilter, String viewPreference, String graphViewType) {
        this.searchPrompt = searchPrompt;
        this.selectedFilter = selectedFilter;
        this.viewPreference = viewPreference;
        this.graphViewType = graphViewType;
        this.dateTime = null;
    }

    public Integer getId() {
        return this.promptId;
    }

    public String getSearchPrompt() {
        return this.searchPrompt;
    }

    public String getSelectedFilter() {
        return this.selectedFilter;
    }

    public String getViewPreference() {
        return this.viewPreference;
    }

    public String getGraphViewType() {
        return this.graphViewType;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}