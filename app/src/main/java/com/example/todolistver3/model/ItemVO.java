package com.example.todolistver3.model;

public class ItemVO {
    public Long id;
    public boolean isdone;
    public String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIsdone() {
        return isdone;
    }

    public void setIsdone(boolean isdone) {
        this.isdone = isdone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
