package com.project.main.entity;

import jakarta.persistence.*;
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"graph_id", "first_paper_id", "second_paper_id"})
)
@Entity
public class CitationEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer edgeId; 

    @ManyToOne
    @JoinColumn(name = "graph_id", nullable = false)
    private Graph graph;  // Foreign Key to Graph

    @ManyToOne
    @JoinColumn(name = "first_paper_id", nullable = false)
    private ResearchPaper firstResearchPaper;  // Foreign Key to ResearchPaper

    @ManyToOne
    @JoinColumn(name = "second_paper_id", nullable = false)
    private ResearchPaper secondResearchPaper;  // Foreign Key to ResearchPaper

    public CitationEdge() {}

    public CitationEdge(Graph graph, ResearchPaper firstResearchPaper, ResearchPaper secondResearchPaper) {
        this.graph = graph;
        this.firstResearchPaper = firstResearchPaper;
        this.secondResearchPaper = secondResearchPaper;
    }

    public Integer getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public ResearchPaper getFirstResearchPaper() {
        return firstResearchPaper;
    }

    public void setFirstResearchPaper(ResearchPaper firstResearchPaper) {
        this.firstResearchPaper = firstResearchPaper;
    }

    public ResearchPaper getSecondResearchPaper() {
        return secondResearchPaper;
    }

    public void setSecondResearchPaper(ResearchPaper secondResearchPaper) {
        this.secondResearchPaper = secondResearchPaper;
    }
}
