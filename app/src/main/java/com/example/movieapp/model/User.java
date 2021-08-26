package com.example.movieapp.model;

public class User {
    private String userId;
    private String email;
    private String fullName;
    private String password;
    private String userName;
    //private int movieId;

    public User() {
    }

    public User(String userId, String email, String fullName, String password, String userName) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.userName = userName;
    }
   /* public int getMovieId() {
        return movieId;
    }*/

    /*public void setMovieId(int movieId) {
        this.movieId = movieId;
    }*/

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
