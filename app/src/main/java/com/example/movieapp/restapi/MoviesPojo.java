package com.example.movieapp.restapi;

import com.example.movieapp.model.Genre;

import java.util.List;

public class MoviesPojo {
    public boolean adult;
    public String backdrop_path;
    public List<Integer> genre_ids;
    public int id;
    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
    public String release_date;
    public String title;
    public boolean video;
    public double vote_average;
    public int vote_count;

    public MoviesPojo(String poster_path, String title) {
        this.poster_path = poster_path;
        this.title = title;
    }
    public MoviesPojo() {
    }


    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
