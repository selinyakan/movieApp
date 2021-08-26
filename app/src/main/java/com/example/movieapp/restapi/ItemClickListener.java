package com.example.movieapp.restapi;


import com.example.movieapp.model.SearchMovie;
import com.example.movieapp.model.SearchSeries;

public interface ItemClickListener {
    void onItemClick(SearchMovie station, int position);
    void onItemClick1(SearchSeries station, int position);
}