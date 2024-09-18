package com.project.main.entity;

import jakarta.persistence.*;
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"graph_id", "paper_id"})
)
@Entity
public class CitationEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer edgeId; 

    // @ManyToOne
    // @JoinColumn(name = "graph_id", nullable = false)
    // private Graph graph;  // Foreign Key to Graph

    @ManyToOne
    @JoinColumn(name = "paper_id", nullable = false)
    private ResearchPaper researchPaper;  // Foreign Key to ResearchPaper

    
    public CitationEdge() {}

    // public CitationEdge(Graph graph, ResearchPaper researchPaper) {
    //     this.graph = graph;
    //     this.researchPaper = researchPaper;
    // }

    public Integer getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
    }

    // public Graph getGraph() {
    //     return graph;
    // }

    // public void setGraph(Graph graph) {
    //     this.graph = graph;
    // }

    public ResearchPaper getResearchPaper() {
        return researchPaper;
    }

    public void setResearchPaper(ResearchPaper researchPaper) {
        this.researchPaper = researchPaper;
    }
}
