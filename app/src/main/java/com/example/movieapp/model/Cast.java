package com.example.movieapp.model;

public class Cast {
    public String original_name;
    public String profile_path;

    public Cast(String original_name, String profile_path) {
        this.original_name = original_name;
        this.profile_path = profile_path;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}



