package com.example.movieapp.model;

import com.example.movieapp.restapi.MoviesPojo;

import java.util.List;

public class MovieListBaseM {
    public int page;
    public List<MoviesPojo> results;
    public List<Cast> cast;
    public List<Crew> crew;
    public List<SearchMovie> results3;
    public List<CategoryItem> resultss;
    public List<Favorites> favorites;
    public List<AllCategory> results1;
    public String poster_path;
    public String overview;
    public String title;
    public double vote_average;
    public List<Genre> genres;
    public List<Genre2> genres1;
    public MovieListBaseM(String poster_path, String title) {
        this.poster_path = poster_path;
        this.title = title;
    }

}
