package com.example.do_an.Models;

public class FavouriteModel extends AbstractModel{
    public static final String TABLE_NAME = "Favourite";
    private int productId;
    private int userId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
