package com.example.do_an.Mapper;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.do_an.DAO.CategoryDAO;
import com.example.do_an.DAO.ImageDAO;
import com.example.do_an.Models.ProductModel;

public class ProductMapper implements IRowMapper<ProductModel> {
    private ImageDAO imageDAO = new ImageDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    @SuppressLint("Range")
    @Override
    public ProductModel mapRow(Cursor cursor) {
        ProductModel productModel = new ProductModel();
        productModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
        productModel.setThumbnail(cursor.getBlob(cursor.getColumnIndex("thumbnail")));
        productModel.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        productModel.setShortDescription(cursor.getString(cursor.getColumnIndex("shortdescription")));
        productModel.setName(cursor.getString(cursor.getColumnIndex("name")));
        productModel.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
        productModel.setPurchases(cursor.getInt(cursor.getColumnIndex("purchases")));
        productModel.setStock(cursor.getInt(cursor.getColumnIndex("stock")));
        productModel.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        productModel.setCreatedDate(cursor.getString(cursor.getColumnIndex("createddate")));
        productModel.setCreatedBy(cursor.getString(cursor.getColumnIndex("createdby")));
        productModel.setModifiedDate(cursor.getString(cursor.getColumnIndex("modifieddate")));
        productModel.setModifiedBy(cursor.getString(cursor.getColumnIndex("modifiedby")));
        productModel.setSizes(cursor.getString(cursor.getColumnIndex("sizes")));
        productModel.setImages(imageDAO.findAllByProductId(productModel.getId()));
        productModel.setCategory(categoryDAO.findOne(cursor.getInt(cursor.getColumnIndex("categoryid"))));
        return productModel;
    }
}
