package com.project.main.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer authorId;

    @Column(nullable = false)
    private String authorName;

    // Owning side of the Many-to-Many relationship with ResearchPaper
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "author_paper", // Join table name
        joinColumns = @JoinColumn(name = "author_id"), // Author ID in the join table
        inverseJoinColumns = @JoinColumn(name = "paper_id") // ResearchPaper ID in the join table
    )
    private Set<ResearchPaper> researchPapers = new HashSet<>();

    public Author() {}

    public Author(String authorName) {
        this.authorName = authorName;
    }

    // Getters and setters...

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Set<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    public void setResearchPapers(Set<ResearchPaper> researchPapers) {
        this.researchPapers = researchPapers;
    }

    // Adding helper methods to handle bi-directionality
    public void addResearchPaper(ResearchPaper paper) {
        this.researchPapers.add(paper);
        paper.getAuthors().add(this);
    }

    public void removeResearchPaper(ResearchPaper paper) {
        this.researchPapers.remove(paper);
        paper.getAuthors().remove(this);
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", researchPapers=" + researchPapers +
                '}';
    }
}
