package com.project.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

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

    public Opinion() {}

    protected Opinion(String opinionVal) {
        this.opinionVal = opinionVal;
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

    @Override
    public String toString() {
        return "Opinion{" +
                "opinionId=" + opinionId +
                ", opinionVal='" + opinionVal + '\'' +
                '}';
    }
}