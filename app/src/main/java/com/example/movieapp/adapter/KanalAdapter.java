package com.example.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.model.Flatrate;
import com.example.movieapp.model.Kanal;

import java.util.List;

public class KanalAdapter extends RecyclerView.Adapter<KanalAdapter.KanalViewHolder> {
    Context mcontext;
    List<Flatrate> mData;

    public KanalAdapter(Context mcontext, List<Flatrate> mData){
        this.mcontext = mcontext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public KanalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_kanal, parent, false);
        return new KanalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KanalViewHolder kanalViewHolder, int position) {
        Glide.with(mcontext).load("https://image.tmdb.org/t/p/w500"+mData.get(position).logo_path).into(kanalViewHolder.img);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public  class  KanalViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public KanalViewHolder(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img_cast);
        }
    }
}
