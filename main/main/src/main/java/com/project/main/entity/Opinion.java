package com.project.main.entity;

import com.project.main.enums.OpinionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Opinion {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer opinionId;

    @Column(name="opinion_val")
    private String opinionVal;

    @Column(name="opinion_type")
    @Enumerated(EnumType.STRING)
    private OpinionType opinionType;

    public Opinion() {}

    protected Opinion(String opinionVal, OpinionType opinionType) {
        this.opinionVal = opinionVal;
        this.opinionType = opinionType;
    }

    public Integer getId() {
        return this.opinionId;
    }

    public void setId(Integer id) {
        this.opinionId = id;
    }

    public String getOpinionVal() {
        return this.opinionVal;
    }

    public void setOpinionVal(String opinionVal) {
        this.opinionVal = opinionVal;
    }

    public OpinionType getOpinionType() {
        return this.opinionType;
    }

    public void setOpinionType(OpinionType opinionType) {
        this.opinionType = opinionType;
    }

    @Override
    public String toString() {
        return "Opinion{" +
                "opinionId=" + opinionId +
                "opinionType=" + opinionType +
                ", opinionVal='" + opinionVal + '\'' +
                '}';
    }
}