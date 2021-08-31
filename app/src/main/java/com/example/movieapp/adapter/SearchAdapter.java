package com.example.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.MovieDetailsPage;
import com.example.movieapp.R;
import com.example.movieapp.model.SearchMovie;
import com.example.movieapp.restapi.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context mcontext;
    List<SearchMovie> mData;
    List<SearchMovie> mData2;
    private ItemClickListener mItemClickListener;

    public SearchAdapter(Context mcontext, List<SearchMovie> mData){
        this.mcontext = mcontext;
        this.mData = mData;
        this.mData2 = mData;
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchMovie movie = mData.get(position);
        holder.setData(movie, position);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mcontext, MovieDetailsPage.class);
                i.putExtra("movieId",String.valueOf(movie.id));
                i.putExtra("isMovie",movie.isMovie);
                mcontext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.movie_img);
            txt = itemView.findViewById(R.id.movie_name);
        }

        public void setData(SearchMovie movie, int position) {
            this.txt.setText(movie.title);
            Glide.with(mcontext).load("https://image.tmdb.org/t/p/w500" +movie.poster_path).into(this.img);
            itemView.setOnClickListener(v -> mItemClickListener.onItemClick(movie,position));

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {

                    mData = mData2;

                } else {

                    ArrayList<SearchMovie> tempFilteredList = new ArrayList<>();

                    for (SearchMovie movie : mData2) {
                        // search for station name
                        if (movie.getTitle().toLowerCase().contains(searchString)) {
                            tempFilteredList.add(new SearchMovie(movie.backdrop_path,movie.overview,movie.poster_path,movie.title,movie.vote_average,movie.id, movie.isMovie));
                        }
                    }

                    mData = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mData = (ArrayList<SearchMovie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
