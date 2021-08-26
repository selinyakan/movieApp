package com.example.movieapp.model;

import java.util.List;

public class SearchSeries {
    public String backdrop_path;
    public int id;
    public String name;
    public String original_name;
    public String poster_path;

    public SearchSeries(String name, String poster_path) {
        this.name = name;
        this.poster_path = poster_path;
    }
}
