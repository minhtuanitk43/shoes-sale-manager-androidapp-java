package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.UserModel;

public class UserContentValues implements IGetContentValues<UserModel> {
    @Override
    public ContentValues getContentValues(UserModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", model.getUserName());
        contentValues.put("password", model.getPassword());
        contentValues.put("fullname", model.getFullName());
        contentValues.put("phone", model.getPhone());
        contentValues.put("address", model.getAddress());
        contentValues.put("createddate", model.getCreatedDate());
        contentValues.put("createdby", model.getCreatedBy());
        contentValues.put("modifieddate", model.getModifiedDate());
        contentValues.put("modifiedby",model.getModifiedBy());
        contentValues.put("permission", model.getPermission());
        contentValues.put("status", model.getStatus());
        return contentValues;
    }
}
