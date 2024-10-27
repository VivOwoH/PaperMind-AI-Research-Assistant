package com.project.main.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ResearchPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer paperId;

    @Column(nullable = false)
    private String semanticPaperId;

    @Column(nullable = true)
    private String title;

    @Column(nullable = true)
    private LocalDateTime publishedDate;

    @Lob
    @Column()
    private String abstractText; // 'abstract' is a keyword, so use 'abstractText'

    @Lob
    @Column()
    private String methodologySummary;

    @Column(nullable = true)
    private String sourceLink;

    @Column(nullable = true)
    private Integer citationsCount;

    @Column(nullable = true)
    private Integer viewsCount;

    // Inverse side of the Many-to-Many relationship with Author
    @ManyToMany(mappedBy = "researchPapers", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    public ResearchPaper() {}

    public ResearchPaper(String semanticPaperId, String title, LocalDateTime publishedDate, String abstractText, String methodologySummary, String sourceLink, Integer citationsCount, Integer viewsCount) {
        this.semanticPaperId = semanticPaperId;
        this.title = title;
        this.publishedDate = publishedDate;
        this.abstractText = abstractText;
        this.methodologySummary = methodologySummary;
        this.sourceLink = sourceLink;
        this.citationsCount = citationsCount;
        this.viewsCount = viewsCount;
    }

    // Getters and setters...

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getSemanticPaperId() {
        return semanticPaperId;
    }

    public void setSemanticPaperId(String semanticPaperId) {
        this.semanticPaperId = semanticPaperId;
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "paperId=" + paperId +
                "semanticPaperId=" + semanticPaperId +
                ", title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                ", abstractText='" + abstractText + '\'' +
                ", methodologySummary='" + methodologySummary + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                ", citationsCount=" + citationsCount +
                ", viewsCount=" + viewsCount +
                '}';
    }

    // Helper methods to handle bi-directionality
    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getResearchPapers().add(this);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getResearchPapers().remove(this);
    }
}
