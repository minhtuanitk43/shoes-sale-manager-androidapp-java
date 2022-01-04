package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.OrderModel;

public class OrderContentValues implements IGetContentValues<OrderModel> {
    @Override
    public ContentValues getContentValues(OrderModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", model.getUserId());
        contentValues.put("address", model.getAddress());
        contentValues.put("phone", model.getPhone());
        contentValues.put("total", model.getTotal());
        contentValues.put("note", model.getNote());
        contentValues.put("status", model.getStatus());
        contentValues.put("customername", model.getCustomerName());
        contentValues.put("createddate", model.getCreatedDate());
        contentValues.put("createdby", model.getCreatedBy());
        contentValues.put("modifieddate", model.getModifiedDate());
        contentValues.put("modifiedby", model.getModifiedBy());
        return contentValues;
    }
}
