package com.example.do_an.Models;

public class OrderDetailModel extends  AbstractModel{
    public static final String TABLE_NAME = "OrderDetail";
    private OrderModel order;
    private int orderId;
    private ProductModel product;
    private int productId;
    private String productSize;
    private int quantity;
    public String getProductSize() {
        return productSize;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }
    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
