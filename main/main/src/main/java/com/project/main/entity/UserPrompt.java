package com.project.main.entity;

import com.project.main.enums.SelectedFilter;
import com.project.main.enums.ViewPreference;
import com.project.main.enums.GraphViewType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDateTime;

@Entity
public class UserPrompt {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer promptId;

    @Column(name="date_time")
    private LocalDateTime dateTime;

    @Column(name="search_prompt")
    private String searchPrompt;

    @Column(name="selected_filter")
    @Enumerated(EnumType.STRING)
    private SelectedFilter selectedFilter;

    @Column(name="view_preference")
    @Enumerated(EnumType.STRING)
    private ViewPreference viewPreference;

    @Column(name="graph_view_type")
    @Enumerated(EnumType.STRING)
    private GraphViewType graphViewType;

    @OneToOne(mappedBy="userPrompt", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private TokenResponse tokenResponse;

    @OneToOne(mappedBy="userPrompt", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private AppResponse appResponse;

    public UserPrompt() {}

    protected UserPrompt(String searchPrompt, SelectedFilter selectedFilter, ViewPreference viewPreference, GraphViewType graphViewType) {
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

    public SelectedFilter getSelectedFilter() {
        return this.selectedFilter;
    }

    public void setSelectedFilter(SelectedFilter selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public ViewPreference getViewPreference() {
        return this.viewPreference;
    }

    public void setViewPreference(ViewPreference viewPreference) {
        this.viewPreference = viewPreference;
    }

    public GraphViewType getGraphViewType() {
        return this.graphViewType;
    }

    public void setGraphViewType(GraphViewType graphViewType) {
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