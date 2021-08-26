package com.example.movieapp.model;

import java.util.List;

//Bu class 06.08 de oluşturuldu.
public class AllCategory {
    Integer categoryId;
    String categoryTitle;
    private List<CategoryItem> categoryItemsList = null;

    public AllCategory(Integer categoryId, String categoryTitle, List<CategoryItem> categoryItemsList) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.categoryItemsList = categoryItemsList;
    }

    public List<CategoryItem> getCategoryItemsList() {
        return categoryItemsList;
    }

    public void setCategoryItemsList(List<CategoryItem> categoryItemsList) {
        this.categoryItemsList = categoryItemsList;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }



}
