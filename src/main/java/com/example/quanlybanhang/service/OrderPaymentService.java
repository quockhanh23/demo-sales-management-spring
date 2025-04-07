package com.example.quanlybanhang.service;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.models.OrderPayment;

import java.util.List;

public interface OrderPaymentService {

    OrderPayment createOrderPayment(OrderPayment orderPayment);

    OrderPayment updateStatusOrderPayment(Long idOrderPayment, OrderPaymentStatus status);

    OrderPayment getDetailOrderPayment(Long idOrderPayment);

    List<OrderPayment> getAllOrderPaymentByIdUserAndOrderPaymentStatus
            (Long idUser, OrderPaymentStatus orderPaymentStatus);

    List<OrderPayment> getAllOrderPaymentByOrderPaymentStatus(OrderPaymentStatus orderPaymentStatus);

}
