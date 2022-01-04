package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.FavouriteModel;

public class FavouriteContentValues implements IGetContentValues<FavouriteModel> {
    @Override
    public ContentValues getContentValues(FavouriteModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("productid", model.getProductId());
        contentValues.put("userid", model.getUserId());
        return contentValues;
    }
}
