package com.example.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.model.Genre;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    Context mcontext;
    List<Genre> mData3;

    public GenreAdapter(Context mcontext, List<Genre> mData3){
        this.mcontext = mcontext;
        this.mData3 = mData3;
    }


    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_genre, parent, false);
        return new GenreAdapter.GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.kategori.setText(mData3.get(position).name);

    }

    @Override
    public int getItemCount() {
        return mData3.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView kategori;

        public GenreViewHolder(@NonNull View itemView){
            super(itemView);
            kategori = itemView.findViewById(R.id.kategori);
        }
    }
}
