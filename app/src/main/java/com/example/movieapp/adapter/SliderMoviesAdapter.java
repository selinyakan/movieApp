package com.example.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.movieapp.MovieDetailsPage;
import com.example.movieapp.R;
import com.example.movieapp.model.SliderMovies;

import java.util.List;

public class SliderMoviesAdapter extends PagerAdapter {

    Context context;
    List<SliderMovies> sliderMoviesList;

    public SliderMoviesAdapter(Context context, List<SliderMovies> sliderMoviesList) {
        this.context = context;
        this.sliderMoviesList = sliderMoviesList;
    }

    @Override
    public int getCount() {
        return sliderMoviesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_movies_layout,null);
        ImageView sliderImage = view.findViewById(R.id.slider_image);
        //here i will use glide library for fetching image from url and set it to image view
        //lets add glide dependency

        Glide.with(context).load(sliderMoviesList.get(position).getImageUrl()).into(sliderImage);
        container.addView(view);

        sliderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+sliderMoviesList.get(position).getMovieName(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MovieDetailsPage.class);
                i.putExtra("movieId",sliderMoviesList.get(position).getId());
                i.putExtra("movieName",sliderMoviesList.get(position).getMovieName());
                i.putExtra("movieImageUrl",sliderMoviesList.get(position).getImageUrl());
                i.putExtra("movieFile",sliderMoviesList.get(position).getFileUrl());
                context.startActivity(i);
            }


        });
        return view;

    }
}
