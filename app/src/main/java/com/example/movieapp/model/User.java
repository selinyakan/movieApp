package com.example.movieapp.model;

import java.util.ArrayList;

public class User {
    private String userId;
    private String email;
    private String fullName;
    private String password;
    private String userName;
    private ArrayList<String> favoriteMovies;
    private ArrayList<String> favoriteTvSeries;
    //private int movieId;
    //buraya da favori tv serileri eklenecek.

    public User(){

    }

    public User(String email,String fullName,String userName) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
    }

    public User(String userId, String email, String fullName, String password, String userName, ArrayList<String> favoriteMovies, ArrayList<String> favoriteTvSeries) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.userName = userName;
        this.favoriteMovies = favoriteMovies;
        this.favoriteTvSeries = favoriteTvSeries;
    }

   /* public int getMovieId() {
        return movieId;
    }*/

    /*public void setMovieId(int movieId) {
        this.movieId = movieId;
    }*/

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(ArrayList<String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public ArrayList<String> getFavoriteTvSeries() {
        return favoriteTvSeries;
    }

    public void setFavoriteTvSeries(ArrayList<String> favoriteTvSeries) {
        this.favoriteTvSeries = favoriteTvSeries;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}