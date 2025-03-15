package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.OrderPayment;
import com.example.quanlybanhang.repository.OrderPaymentRepository;
import com.example.quanlybanhang.service.OrderPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    public OrderPayment createOrderPayment(OrderPayment orderPayment) {
        return orderPaymentRepository.save(orderPayment);
    }

    @Override
    public OrderPayment updateStatusOrderPayment(Long idOrderPayment, String status) {
        OrderPayment orderPayment = getDetailOrderPayment(idOrderPayment);
        return orderPaymentRepository.save(orderPayment);
    }

    @Override
    public OrderPayment getDetailOrderPayment(Long idOrderPayment) {
        Optional<OrderPayment> optionalOrderPayment = orderPaymentRepository.findById(idOrderPayment);
        if (optionalOrderPayment.isEmpty()) throw new InvalidException("Không tìm thấy");
        return optionalOrderPayment.get();
    }

    @Override
    public List<OrderPayment> getAllOrderPaymentByIdUserAndOrderPaymentStatus
            (Long idUser, OrderPaymentStatus orderPaymentStatus) {
        return orderPaymentRepository.getAllOrderPaymentByIdUserAndOrderPaymentStatus(idUser, orderPaymentStatus);
    }
}
