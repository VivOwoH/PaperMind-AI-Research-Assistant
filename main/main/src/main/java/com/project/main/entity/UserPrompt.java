package com.project.main.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
public class UserPrompt {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer promptId;

    @Column(name="search_prompt")
    private String searchPrompt;

    @Column(name="date_time")
    private LocalDateTime dateTime;

    @Column(name="selected_filter")
    private String selectedFilter;

    @Column(name="view_preference")
    private String viewPreference;

    @Column(name="graph_view_type")
    private String graphViewType;

    @OneToOne(mappedBy="userPrompt", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private TokenResponse tokenResponse;

    @OneToOne(mappedBy="userPrompt", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private AppResponse appResponse;

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

    public void setId(Integer id) {
        this.promptId = id;
    }

    public String getSearchPrompt() {
        return this.searchPrompt;
    }

    public void setSearchPrompt(String searchPrompt) {
        this.searchPrompt = searchPrompt;
    }

    public String getSelectedFilter() {
        return this.selectedFilter;
    }

    public void setSelectedFilter(String selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public String getViewPreference() {
        return this.viewPreference;
    }

    public void setViewPreference(String viewPreference) {
        this.viewPreference = viewPreference;
    }

    public String getGraphViewType() {
        return this.graphViewType;
    }

    public void setGraphViewType(String graphViewType) {
        this.graphViewType = graphViewType;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "UserPrompt{" +
                "promptId=" + promptId +
                ", searchPrompt='" + searchPrompt + '\'' +
                ", dateTime=" + dateTime +
                ", selectedFilter='" + selectedFilter + '\'' +
                ", viewPreference='" + viewPreference + '\'' +
                ", graphViewType='" + graphViewType + '\'' +
                '}';
    }
}