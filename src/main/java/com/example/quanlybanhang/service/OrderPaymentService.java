package com.example.quanlybanhang.service;

import com.example.quanlybanhang.models.OrderPayment;

public interface OrderPaymentService {

    void createOrderPayment(OrderPayment orderPayment);

    void updateStatusOrderPayment(String status);

    OrderPayment findById(Long idOrderPayment);
}
