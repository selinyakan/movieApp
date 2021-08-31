package com.example.movieapp.model;

//Bu sayfa 11/08 de oluşturuldu

public class Favorites {
    public String poster_path;
    public boolean isMovie;
    public String id1;

    public Favorites(String poster_path, boolean isMovie, String id1) {
        this.poster_path = poster_path;
        this.isMovie = isMovie;
        this.id1 = id1;
    }

}
