package com.example.do_an.GetContentValues;

import android.content.ContentValues;

import com.example.do_an.Models.ProductModel;

public class ProductContentValues implements IGetContentValues<ProductModel> {
    @Override
    public ContentValues getContentValues(ProductModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", model.getName());
        contentValues.put("categoryid", model.getCategory().getId());
        contentValues.put("price", model.getPrice());
        contentValues.put("thumbnail", model.getThumbnail());
        contentValues.put("shortdescription", model.getShortDescription());
        contentValues.put("description", model.getDescription());
        contentValues.put("purchases", model.getPurchases());
        contentValues.put("stock", model.getStock());
        contentValues.put("createddate", model.getCreatedDate());
        contentValues.put("createdby", model.getCreatedBy());
        contentValues.put("modifieddate", model.getModifiedDate());
        contentValues.put("modifiedby", model.getModifiedBy());
        contentValues.put("sizes", model.getSizes());
        contentValues.put("status", model.getStatus());
        return contentValues;
    }
}
