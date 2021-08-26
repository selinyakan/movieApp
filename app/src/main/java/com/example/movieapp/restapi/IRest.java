package com.example.movieapp.restapi;

import com.example.movieapp.model.CategoryItem;
import com.example.movieapp.model.MovieListBaseM;
import com.example.movieapp.model.MovieListBaseM3;
import com.example.movieapp.model.ProviderList;
import com.example.movieapp.model.SeriesListBaseM;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRest {

    // https://api.themoviedb.org/3/     ?s=api&t=getPatientImages
    @GET("movie/{id}?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getMovieCredits(
            @Path("id") String id

    );

    @GET("movie/{id}/watch/providers?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<ProviderList> getMovieCreditsProviders(
            @Path("id") String id

    );

    @GET("movie/{id}/credits?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getMovieCreditsCasts(
            @Path("id") String id

    );

    @GET("tv/{id}/credits?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSeriesCreditsCasts(
            @Path("id") String id

    );

    @GET("discover/movie?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getMovieList(
            @Query("page") String pageNumber

    );

    @GET("movie/popular?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getPopularMovieList(
            @Query("page") String pageNumber
    );

    @GET("search/movie?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM3> getSearchMovieList(
            @Query("query") String query
    );

    @GET("search/tv?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSearchSeriesList(
            @Query("query") String query
    );

    @GET("tv/{id}?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSeriesCredits(
            @Path("id") String id

    );

    @GET("discover/tv?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSeriesList(
            @Query("page") String pageNumber

    );

    @GET("movie/popular?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getPopularMovieListByPage(
            @Query("page") String page
    );

}
