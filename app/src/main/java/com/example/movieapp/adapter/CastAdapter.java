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
import com.example.movieapp.model.Cast;

import java.util.List;
//09.08de oluşuturuldu
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    Context mcontext;
    List<Cast> mData;

    public CastAdapter(Context mcontext, List<Cast> mData){
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_cast, viewGroup, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder castViewHolder, int position) {
        Glide.with(mcontext).load("https://image.tmdb.org/t/p/w500"+mData.get(position).profile_path).into(castViewHolder.img_cast);
        castViewHolder.name_cast.setText(mData.get(position).original_name);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  class  CastViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cast;
        TextView name_cast;

        public CastViewHolder(@NonNull View itemView){
            super(itemView);
            img_cast = itemView.findViewById(R.id.img_cast);
            name_cast = itemView.findViewById(R.id.name_cast);
        }
    }
}
