package com.extracker.model;

public class Categories {
    private int id;
    private String title;
    private String description;

    public Categories() {
        this.id = 0;
        this.title = "";
        this.description = "";
    }

    public Categories(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Categories(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {  
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
