package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.Models.UserModel;

public class UserMapper implements IRowMapper<UserModel> {
    @SuppressLint("Range")
    @Override
    public UserModel mapRow(Cursor cursor) {
        UserModel user = new UserModel();
        user.setId(cursor.getInt(cursor.getColumnIndex("id")));
        user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
        user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        user.setFullName(cursor.getString(cursor.getColumnIndex("fullname")));
        user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        user.setPermission(cursor.getString(cursor.getColumnIndex("permission")));
        user.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        user.setCreatedDate(cursor.getString(cursor.getColumnIndex("createddate")));
        user.setCreatedBy(cursor.getString(cursor.getColumnIndex("createdby")));
        user.setModifiedDate(cursor.getString(cursor.getColumnIndex("modifieddate")));
        user.setModifiedBy(cursor.getString(cursor.getColumnIndex("modifiedby")));
        return user;
    }
}
