package com.project.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;

@Entity
public class OpinionEdge {

    @EmbeddedId
    private OpinionEdgeId id;

    @ManyToOne
    @MapsId("graphId")
    @JoinColumn(name = "graph_id", nullable=false)
    private Graph graph;

    @ManyToOne
    @MapsId("opinionId")
    @JoinColumn(name = "opinion_id", nullable=false)
    private Opinion opinion;

    public OpinionEdge() {}

    protected OpinionEdge(OpinionEdgeId id, Graph graph, Opinion opinion) {
        this.id = id;
        this.graph = graph;
        this.opinion = opinion;
    }

    public OpinionEdgeId getId() {
        return this.id;
    }

    public void setId(OpinionEdgeId id) {
        this.id = id;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Opinion getOpinion() {
        return this.opinion;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    @Override
    public String toString() {
        return "OpinionEdge{" +
                "Id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpinionEdge that = (OpinionEdge) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}