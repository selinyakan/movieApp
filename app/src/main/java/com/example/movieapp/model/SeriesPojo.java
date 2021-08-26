package com.example.movieapp.model;

public class SeriesPojo {
    public String backdrop_path;
    public int id;
    public String name;
    public String original_name;
    public String overview;
    public String poster_path;
    public double vote_average;

    public SeriesPojo(int id, String name, String poster_path) {
        this.id = id;
        this.name = name;
        this.poster_path = poster_path;
    }
}
