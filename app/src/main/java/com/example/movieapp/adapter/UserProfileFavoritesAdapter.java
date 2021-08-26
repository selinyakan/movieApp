package com.example.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.model.Favorites;

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
    public void onBindViewHolder(@NonNull UserProfileFavoritesAdapter.UserViewHolder holder, int position) {
        Glide.with(context).load(favoritesList.get(position).getFavoriteMovie()).into(holder.favoriteMovie);
        holder.favoriteMovieName.setText(favoritesList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView favoriteMovie;
        TextView favoriteMovieName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteMovie = itemView.findViewById(R.id.favorites_movie_img);
            favoriteMovieName = itemView.findViewById(R.id.favorites_movie_name);

        }
    }
}