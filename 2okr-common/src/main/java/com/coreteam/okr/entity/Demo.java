package com.coreteam.okr.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * demo
 * @author 
 */
public class Demo implements Serializable {
    private Integer id;

    private String name;

    private Integer gmtCreateBy;

    private LocalDateTime gmtCreate;

    private Integer gmtModifiedBy;

    private LocalDateTime gmtModified;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGmtCreateBy() {
        return gmtCreateBy;
    }

    public void setGmtCreateBy(Integer gmtCreateBy) {
        this.gmtCreateBy = gmtCreateBy;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getGmtModifiedBy() {
        return gmtModifiedBy;
    }

    public void setGmtModifiedBy(Integer gmtModifiedBy) {
        this.gmtModifiedBy = gmtModifiedBy;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gmtCreateBy=" + gmtCreateBy +
                ", gmtCreate=" + gmtCreate +
                ", gmtModifiedBy=" + gmtModifiedBy +
                ", gmtModified=" + gmtModified +
                '}';
    }
}