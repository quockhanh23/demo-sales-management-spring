package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.models.OrderPayment;
import com.example.quanlybanhang.repository.OrderPaymentRepository;
import com.example.quanlybanhang.service.OrderPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    public void createOrderPayment(OrderPayment orderPayment) {
        orderPaymentRepository.save(orderPayment);
    }

    @Override
    public void updateStatusOrderPayment(String status) {

    }

    @Override
    public OrderPayment findById(Long idOrderPayment) {
        return null;
    }
}
