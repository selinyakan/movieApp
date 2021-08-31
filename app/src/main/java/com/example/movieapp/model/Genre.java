package com.example.movieapp.model;

public class Genre {
    public String name;
    public Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Genre(String name) {
        this.name = name;
    }
}
