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
    public List<AllCategory> results1;
    public String poster_path;
    public String overview;
    public String title;
    public double vote_average;
    public List<Genre> genres;

}
