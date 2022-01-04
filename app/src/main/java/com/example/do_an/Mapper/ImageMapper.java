package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.ImageModel;

public class ImageMapper implements IRowMapper<ImageModel> {
    @SuppressLint("Range")
    @Override
    public ImageModel mapRow(Cursor cursor) {
        ImageModel imageModel = new ImageModel();
        imageModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        imageModel.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
        imageModel.setProductId(cursor.getInt(cursor.getColumnIndex("productid")));
        return imageModel;
    }
}
