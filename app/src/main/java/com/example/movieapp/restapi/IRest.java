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
    //MOVIE DETAIL
    @GET("movie/{id}?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getMovieCredits(
            @Path("id") String id
    );

    //MOVIE LIST
    @GET("discover/movie?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getMovieList(
            @Query("page") String pageNumber
    );

    //POPULAR MOVIE
    @GET("movie/popular?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getPopularMovieList(
            @Query("page") String pageNumber
    );
    //https://api.themoviedb.org/3/discover/movie?api_key=e92bb47edd2e88ee921dc35b9a51d66a&with_genres=27
    //POPULAR MOVIE
    @GET("discover/movie?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getPopularList(
            @Query("with_genres") String pageNumber
    );

    //MOVIE CAST
    @GET("movie/{id}/credits?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getMovieCreditsCasts(
            @Path("id") String id
    );

    //PROVIDER
    @GET("movie/{id}/watch/providers?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<ProviderList> getMovieCreditsProviders(
            @Path("id") String id
    );

    //SERIES PROVIDER
    @GET("tv/{id}/watch/providers?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<ProviderList> getMovieCreditsProvidersSeries(
            @Path("id") String id
    );

    //SERIES CAST
    @GET("tv/{id}/credits?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSeriesCreditsCasts(
            @Path("id") String id
    );

    //SEARCH
    @GET("search/movie?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM3> getSearchMovieList(
            @Query("query") String query
    );

    //SEARCH SERIES
    @GET("search/tv?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSearchSeriesList(
            @Query("query") String query
    );

    //SERIES DETAIL
    @GET("tv/{id}?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSeriesCredits(
            @Path("id") String id
    );

    //SERIES LIST
    @GET("discover/tv?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<SeriesListBaseM> getSeriesList(
            @Query("page") String pageNumber
    );

    @GET("movie/popular?api_key="+AppConsts.apiKey+"language=tr-TR")
    Call<MovieListBaseM> getPopularMovieListByPage(
            @Query("page") String page
    );

}
