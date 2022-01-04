package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.CategoryModel;

public class CategoryMapper implements IRowMapper<CategoryModel> {
    @SuppressLint("Range")
    @Override
    public CategoryModel mapRow(Cursor cursor) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        categoryModel.setName(cursor.getString(cursor.getColumnIndex("name")));
        categoryModel.setKeySearch(cursor.getString(cursor.getColumnIndex("keysearch")));
        categoryModel.setCreatedDate(cursor.getString(cursor.getColumnIndex("createddate")));
        categoryModel.setCreatedBy(cursor.getString(cursor.getColumnIndex("createdby")));
        categoryModel.setModifiedDate(cursor.getString(cursor.getColumnIndex("modifieddate")));
        categoryModel.setModifiedBy(cursor.getString(cursor.getColumnIndex("modifiedby")));
        return categoryModel;
    }
}
