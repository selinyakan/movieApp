package com.example.movieapp.model;

//Bu sayfa 11/08 de oluşturuldu

public class Favorites {
    String name;
    String favoriteMovie;

    public Favorites(String name, String favoriteMovie) {
        this.name = name;
        this.favoriteMovie = favoriteMovie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setFavoriteMovie(String favoriteMovie) {
        this.favoriteMovie = favoriteMovie;
    }



}
