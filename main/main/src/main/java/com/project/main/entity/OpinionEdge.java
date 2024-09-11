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

    protected OpinionEdge() {}

    public OpinionEdgeId getId() {
        return this.id;
    }

    public void setId(OpinionEdgeId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OpinionEdge{" +
                "Id=" + id +
                '}';
    }
}