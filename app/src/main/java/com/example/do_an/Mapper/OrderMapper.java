package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.OrderModel;

public class OrderMapper implements IRowMapper<OrderModel> {
    @SuppressLint("Range")
    @Override
    public OrderModel mapRow(Cursor cursor) {
        OrderModel orderModel = new OrderModel();
        orderModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        orderModel.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
        orderModel.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        orderModel.setTotal(cursor.getDouble(cursor.getColumnIndex("total")));
        orderModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        orderModel.setNote(cursor.getString(cursor.getColumnIndex("note")));
        orderModel.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        orderModel.setCustomerName(cursor.getString(cursor.getColumnIndex("customername")));
        orderModel.setCreatedDate(cursor.getString(cursor.getColumnIndex("createddate")));
        orderModel.setCreatedBy(cursor.getString(cursor.getColumnIndex("createdby")));
        orderModel.setModifiedDate(cursor.getString(cursor.getColumnIndex("modifieddate")));
        orderModel.setModifiedBy(cursor.getString(cursor.getColumnIndex("modifiedby")));
        return orderModel;
    }
}
