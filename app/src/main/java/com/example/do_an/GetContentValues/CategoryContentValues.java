package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.CategoryModel;

public class CategoryContentValues implements IGetContentValues<CategoryModel> {
    @Override
    public ContentValues getContentValues(CategoryModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", model.getName());
        contentValues.put("keysearch", model.getKeySearch());
        contentValues.put("createddate", model.getCreatedDate());
        contentValues.put("createdby", model.getCreatedBy());
        contentValues.put("modifieddate", model.getModifiedDate());
        contentValues.put("modifiedby", model.getModifiedBy());
        return contentValues;
    }
}
