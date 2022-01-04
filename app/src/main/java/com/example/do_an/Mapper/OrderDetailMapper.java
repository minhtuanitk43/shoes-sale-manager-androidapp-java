package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.OrderDetailModel;
import com.example.do_an.Service.ProductService;

public class OrderDetailMapper implements IRowMapper<OrderDetailModel> {
    @SuppressLint("Range")
    @Override
    public OrderDetailModel mapRow(Cursor cursor) {
        OrderDetailModel model = new OrderDetailModel();
        model.setId(cursor.getInt(cursor.getColumnIndex("id")));
        model.setProduct(ProductService.getInstance().findOne(cursor.getInt(cursor.getColumnIndex("productid"))));
        model.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
        model.setOrderId(cursor.getInt(cursor.getColumnIndex("orderid")));
        model.setProductSize(cursor.getString(cursor.getColumnIndex("productSize")));
        return model;
    }
}
