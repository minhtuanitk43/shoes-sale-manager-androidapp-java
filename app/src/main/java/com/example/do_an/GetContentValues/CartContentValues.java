package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.CartModel;

public class CartContentValues implements IGetContentValues<CartModel> {
    @Override
    public ContentValues getContentValues(CartModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", model.getUser().getId());
        contentValues.put("productid", model.getProduct().getId());
        contentValues.put("quantity", model.getQuantity());
        contentValues.put("productSize", model.getProductSize());
        return contentValues;
    }
}
