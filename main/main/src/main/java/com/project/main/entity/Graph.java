package com.project.main.entity;

import com.project.main.enums.GraphType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Graph {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer graphId;

    @OneToOne
    @JoinColumn(name="app_response_id", referencedColumnName = "id", nullable=false)
    private AppResponse appResponse;

    @Column(name="graph_type")
    @Enumerated(EnumType.STRING)
    private GraphType graphType;

    public Graph() {}

    protected Graph(AppResponse appResponse, GraphType graphType) {
        this.appResponse = appResponse;
        this.graphType = graphType;
    }

    public Integer getId() {
        return this.graphId;
    }

    public void setId(Integer id) {
        this.graphId = id;
    }

    public AppResponse getAppResponse() {
        return this.appResponse;
    }

    public void setAppResponse(AppResponse appResponse) {
        this.appResponse = appResponse;
    }

    public GraphType getGraphType() {
        return this.graphType;
    }

    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "graphId=" + graphId +
                ", appResponse=" + appResponse.toString() +
                ", graphType='" + graphType + '\'' +
                '}';
    }
}