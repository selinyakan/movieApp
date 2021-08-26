package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapp.adapter.CastAdapter;
import com.example.movieapp.adapter.SearchAdapter;
import com.example.movieapp.adapter.SearchSeriesAdapter;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.MovieListBaseM;
import com.example.movieapp.model.MovieListBaseM3;
import com.example.movieapp.model.SearchMovie;
import com.example.movieapp.model.SearchSeries;
import com.example.movieapp.model.SeriesListBaseM;
import com.example.movieapp.model.SliderMovies;
import com.example.movieapp.restapi.IRest;
import com.example.movieapp.restapi.RetrofitPage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPage extends AppCompatActivity {
    private RecyclerView rcy;
    private SearchAdapter searchAdapter;
    private SearchSeriesAdapter searchAdapter1;
    List mData, mData1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        rcy = findViewById(R.id.search_recycler);
        mData = new ArrayList();
        searchAdapter = new SearchAdapter(this, mData);
        mData1 = new ArrayList<>();
        searchAdapter1 = new SearchSeriesAdapter(this,mData1);

        setupRvSearch("");
        //setupRvSearchSeries("");

    }

    private void setupRvSearch(String input) {
        List<SearchMovie> mData = new ArrayList<>();
        Call<MovieListBaseM3> call = prepareRetrofit().getSearchMovieList(input);
        call.enqueue(new Callback<MovieListBaseM3>() {
            @Override
            public void onResponse(Call<MovieListBaseM3> call, Response<MovieListBaseM3> response) {
                MovieListBaseM3 result = response.body();

                if (response.isSuccessful() && result != null) {

                    for (int i = 0; i < result.results.size(); i++) {
                        mData.add(new SearchMovie("https://image.tmdb.org/t/p/w500"
                                + result.results.get(i).backdrop_path, result.results.get(i).overview,
                                "https://image.tmdb.org/t/p/w500" + result.results.get(i).poster_path
                                , result.results.get(i).title, result.results.get(i).vote_average, result.results.get(i).id));
                        //Log.d("","error catched at getPatientTCNo: "+result.results.get(i).title);
                    }
                    searchAdapter = new SearchAdapter(SearchPage.this, mData);
                    rcy.setAdapter(searchAdapter);
                    rcy.setLayoutManager(new LinearLayoutManager(SearchPage.this, LinearLayoutManager.VERTICAL, false));

                }
            }

            @Override
            public void onFailure(Call<MovieListBaseM3> call, Throwable t) {
                Log.d("", "error catched at getPatientTCNo: " + t);
            }
        });


    }
    private void setupRvSearchSeries(String input) {
        List<SearchSeries> mData1 = new ArrayList<>();
        Call<SeriesListBaseM> call = prepareRetrofit().getSearchSeriesList(input);
        call.enqueue(new Callback<SeriesListBaseM>() {
            @Override
            public void onResponse(Call<SeriesListBaseM> call, Response<SeriesListBaseM> response) {
                SeriesListBaseM result2 = response.body();

                if (response.isSuccessful() && result2 != null) {

                    for (int i = 0; i < result2.results.size(); i++) {
                        mData1.add(new SearchSeries(result2.results2.get(i).name,"https://image.tmdb.org/t/p/w500"
                                + result2.results2.get(i).poster_path));
                    }
                    searchAdapter1 = new SearchSeriesAdapter(SearchPage.this, mData1);
                    rcy.setAdapter(searchAdapter1);
                    rcy.setLayoutManager(new LinearLayoutManager(SearchPage.this, LinearLayoutManager.VERTICAL, false));

                }
            }

            @Override
            public void onFailure(Call<SeriesListBaseM> call, Throwable t) {
                Log.d("", "error catched at getPatientTCNo: " + t);
            }
        });


    }

    public static IRest prepareRetrofit() {
        Retrofit retrofit = RetrofitPage.getRetrofit();
        return retrofit.create(IRest.class);
    }


    @SuppressLint("ResourceType")
    public boolean onCreateOptionsMenu(Menu menu1) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu1);

        MenuItem searchItem = menu1.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = null;
        if (searchItem != null) {
            searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                //setupRvSearchSeries(newText);
                setupRvSearch(newText);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu1);
    }

    public void onItemClick(SearchMovie station, int position) {
        Toast.makeText(this, station.getTitle() + " Clicked.", Toast.LENGTH_SHORT).show();
    }
    public void onItemClick1(SearchSeries station, int position) {
        Toast.makeText(this, station.name + " Clicked.", Toast.LENGTH_SHORT).show();
    }

    public void onRefresh() {
        mData.clear();
        setupRvSearch("");
        //setupRvSearchSeries("");
    }
}
