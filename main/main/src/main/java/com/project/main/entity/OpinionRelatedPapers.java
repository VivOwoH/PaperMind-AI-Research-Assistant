package com.project.main.entity;

import jakarta.persistence.*;

@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"opinion_id", "paper_id"})
)
@Entity
public class OpinionRelatedPapers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer opinionRelatedPaperId;  // Primary Key auto-generated

    @ManyToOne
    @JoinColumn(name = "opinion_id", nullable = false)
    private Opinion opinion;  // Foreign Key to Opinion

    @ManyToOne
    @JoinColumn(name = "paper_id", nullable = false)
    private ResearchPaper researchPaper;  // Foreign Key to ResearchPaper

    // Constructors, Getters, and Setters
    public OpinionRelatedPapers() {}

    public OpinionRelatedPapers(Opinion opinion, ResearchPaper researchPaper) {
        this.opinion = opinion;
        this.researchPaper = researchPaper;
    }

    public Integer getOpinionRelatedPaperId() {
        return opinionRelatedPaperId;
    }

    public void setOpinionRelatedPaperId(Integer opinionRelatedPaperId) {
        this.opinionRelatedPaperId = opinionRelatedPaperId;
    }

    public Opinion getOpinion() {
        return opinion;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public ResearchPaper getResearchPaper() {
        return researchPaper;
    }

    public void setResearchPaper(ResearchPaper researchPaper) {
        this.researchPaper = researchPaper;
    }
}

/**
 * This would ensure that each graph_id and paper_id combination is unique without needing to create a composite primary key. You can apply the same for opinion_id and paper_id in the OpinionRelatedPapers entity.

 */