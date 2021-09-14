package com.example.pharmacyproject.Models;

public class OrderItem {
    private Medicine medicine;
    private String OrderItemId ;
    private String uid ;
    private double order ;
    private String descriptionOrder ;
    private boolean isReceipt ;

    public OrderItem() {
    }

    public OrderItem(Medicine medicine, String orderItemId, String uid, double order, String descriptionOrder, boolean isReceipt) {
        this.medicine = medicine;
        OrderItemId = orderItemId;
        this.uid = uid;
        this.order = order;
        this.descriptionOrder = descriptionOrder;
        this.isReceipt = isReceipt;
    }

    public String getDescriptionOrder() {
        return descriptionOrder;
    }

    public void setDescriptionOrder(String descriptionOrder) {
        this.descriptionOrder = descriptionOrder;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public String getOrderItemId() {
        return OrderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        OrderItemId = orderItemId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getOrder() {
        return order;
    }

    public void setOrder(double order) {
        this.order = order;
    }

    public boolean isReceipt() {
        return isReceipt;
    }

    public void setReceipt(boolean receipt) {
        isReceipt = receipt;
    }
}
