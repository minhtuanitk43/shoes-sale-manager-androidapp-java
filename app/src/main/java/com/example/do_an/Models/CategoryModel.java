package com.example.do_an.Models;

public class CategoryModel extends  AbstractModel{
    public static final String TABLE_NAME = "Category";
    private String name;
    private String keySearch;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }
}
