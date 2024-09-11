package com.project.main.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OpinionEdgeId implements Serializable {
    private Integer edgeId;
    private Integer graphId;
    private Integer opinionId;

    public Integer getEdgeId() {
        return this.edgeId;
    }

    public void setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
    }

    public Integer getGraphId() {
        return this.graphId;
    }

    public void setGraphId(Integer graphId) {
        this.graphId = graphId;
    }

    public Integer getOpinionId() {
        return this.opinionId;
    }

    public void setOpinionId(Integer opinionId) {
        this.opinionId = opinionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpinionEdgeId that = (OpinionEdgeId) o;
        return Objects.equals(edgeId, that.edgeId) &&
                Objects.equals(graphId, that.graphId) &&
                Objects.equals(opinionId, that.opinionId);
    }
}