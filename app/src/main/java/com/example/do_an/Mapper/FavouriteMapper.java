package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.FavouriteModel;

public class FavouriteMapper implements IRowMapper<FavouriteModel> {
    @SuppressLint("Range")
    @Override
    public FavouriteModel mapRow(Cursor cursor) {
        FavouriteModel favouriteModel = new FavouriteModel();
        favouriteModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        favouriteModel.setProductId(cursor.getInt(cursor.getColumnIndex("productid")));
        favouriteModel.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
        return favouriteModel;
    }
}
