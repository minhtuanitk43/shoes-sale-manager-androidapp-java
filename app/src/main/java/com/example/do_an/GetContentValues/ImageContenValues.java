package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.ImageModel;

public class ImageContenValues implements IGetContentValues<ImageModel> {
    @Override
    public ContentValues getContentValues(ImageModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("productid", model.getProductId());
        contentValues.put("image", model.getImage());
        return contentValues;
    }
}
