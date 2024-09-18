package com.project.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ResearchPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer paperId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime publishedDate;

    @Column(length = 2000)
    private String abstractText; // 'abstract' is a keyword, so use 'abstractText'

    @Column(length = 2000)
    private String methodologySummary; // Simplified by AI for a general audience

    @Column(nullable = false)
    private String sourceLink; // URL to the paper

    @Column(nullable = false)
    private Integer citationsCount;

    @Column(nullable = false)
    private Integer viewsCount;

    // Constructors, Getters, Setters

    public ResearchPaper() {}

    public ResearchPaper(String title, LocalDateTime publishedDate, String abstractText, String methodologySummary, String sourceLink, Integer citationsCount, Integer viewsCount) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.abstractText = abstractText;
        this.methodologySummary = methodologySummary;
        this.sourceLink = sourceLink;
        this.citationsCount = citationsCount;
        this.viewsCount = viewsCount;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getMethodologySummary() {
        return methodologySummary;
    }

    public void setMethodologySummary(String methodologySummary) {
        this.methodologySummary = methodologySummary;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public Integer getCitationsCount() {
        return citationsCount;
    }

    public void setCitationsCount(Integer citationsCount) {
        this.citationsCount = citationsCount;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "paperId=" + paperId +
                ", title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                ", abstractText='" + abstractText + '\'' +
                ", methodologySummary='" + methodologySummary + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                ", citationsCount=" + citationsCount +
                ", viewsCount=" + viewsCount +
                ", authors=" + authors +
                '}';
    }

    // Many-to-Many relationship with Author
    @ManyToMany(mappedBy = "researchPapers")
    private Set<Author> authors = new HashSet<>();

    // Constructors, Getters, and Setters

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

}
