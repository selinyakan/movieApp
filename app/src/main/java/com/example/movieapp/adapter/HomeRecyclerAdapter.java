package com.example.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.model.AllCategory;
import com.example.movieapp.model.CategoryItem;

import java.util.List;

// Bu class 06.08de oluşturuldu.

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder> {

    Context context;
    List<AllCategory> allCategoryList;



    public HomeRecyclerAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.home_recycler_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.categoryName.setText(allCategoryList.get(position).getCategoryTitle());
        //06.08 gün bitiminde eklendi
        setItemRecyler(holder.itemRecyler,allCategoryList.get(position).getCategoryItemsList());



    }
    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public static final class HomeViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        //06.08 gün bitiminde eklendi
        RecyclerView itemRecyler;

        public HomeViewHolder(@NonNull View itemView){
            super(itemView);
            //06.08 gün bitiminde eklendi
            categoryName = itemView.findViewById(R.id.item_category);
            itemRecyler = itemView.findViewById(R.id.item_recycler);


        }

    }
    //06.08 gün bitiminde eklendi
    private void setItemRecyler(RecyclerView recyclerView, List<CategoryItem> categoryItemList){
        ItemRecyclerAdapter itemRecyclerAdapter = new ItemRecyclerAdapter(context,categoryItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);
    }
}
