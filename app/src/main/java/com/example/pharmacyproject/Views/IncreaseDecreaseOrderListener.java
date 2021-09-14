package com.example.pharmacyproject.Views;


import com.example.pharmacyproject.Models.OrderItem;

public interface IncreaseDecreaseOrderListener {
    void onIncreaseOrder(OrderItem orderItem);
    void onDecreaseOrder(OrderItem orderItem);


}
