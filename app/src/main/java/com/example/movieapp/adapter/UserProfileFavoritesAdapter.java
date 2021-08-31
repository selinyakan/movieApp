package com.example.movieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.MovieDetailsPage;
import com.example.movieapp.R;
import com.example.movieapp.UserProfileActivity;
import com.example.movieapp.model.Favorites;
import com.example.movieapp.model.MovieListBaseM;

import java.util.List;

//Bu sayfa 11/08 de oluşturuldu

public class UserProfileFavoritesAdapter extends RecyclerView.Adapter<UserProfileFavoritesAdapter.UserViewHolder> {
    Context context;
    List<Favorites> favoritesList;

    public UserProfileFavoritesAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public UserProfileFavoritesAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userprofile_grid_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileFavoritesAdapter.UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Favorites favorities = favoritesList.get(position) ;
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" +favorities.poster_path).into(holder.favoriteMovie);
        Log.d("", "yeter artık BU KADARI FAZLA " + favorities.id1);
        Log.d("", "yeter artık BU KADARI FAZLA " + favorities.id1);
        Log.d("", "yeter artık BU KADARI FAZLA " + favorities.id1);
        Log.d("", "yeter artık BU KADARI FAZLA " + favorities.id1);


        holder.favoriteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MovieDetailsPage.class);
                Log.d("", "yeter artık BU KADARI FAZLA DEĞİLLLLL " + favorities.id1);
                Log.d("", "yeter artık BU KADARI FAZLA DEĞİLLLLLLL" + favorities.id1);
                Log.d("", "yeter artık BU KADARI FAZLA DEĞİLLLLL " + favorities.id1);

                i.putExtra("movieId",favorities.id1);
                i.putExtra("isMovie",favorities.isMovie);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView favoriteMovie;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteMovie = itemView.findViewById(R.id.favorites_movie_img);

        }
    }
}