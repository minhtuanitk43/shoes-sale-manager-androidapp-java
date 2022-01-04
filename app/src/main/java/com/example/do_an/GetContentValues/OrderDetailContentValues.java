package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.OrderDetailModel;

public class OrderDetailContentValues implements IGetContentValues<OrderDetailModel> {
    @Override
    public ContentValues getContentValues(OrderDetailModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("orderid", model.getOrderId());
        contentValues.put("productid", model.getProductId());
        contentValues.put("quantity", model.getQuantity());
        contentValues.put("productSize", model.getProductSize());
        return contentValues;
    }
}
