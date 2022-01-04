package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.CartModel;
import com.example.do_an.Service.ProductService;
import com.example.do_an.Service.UserService;

public class CartMapper implements IRowMapper<CartModel> {
    @SuppressLint("Range")
    @Override
    public CartModel mapRow(Cursor cursor) {
        CartModel cartModel = new CartModel();
        cartModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        cartModel.setUser(UserService.getInstance().findOne(cursor.getInt(
                cursor.getColumnIndex("userid"))));
        cartModel.setProduct(ProductService.getInstance().findOne(
                cursor.getInt(cursor.getColumnIndex("productid"))));
        cartModel.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
        cartModel.setProductSize(cursor.getString(cursor.getColumnIndex("productSize")));
        return cartModel;
    }
}
