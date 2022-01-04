package com.example.do_an.Models;

public class ImageModel extends AbstractModel{
    public static final String TABLE_NAME = "Image";
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
