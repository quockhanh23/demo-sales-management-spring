package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.OrderPayment;
import com.example.quanlybanhang.repository.OrderPaymentRepository;
import com.example.quanlybanhang.service.OrderPaymentService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {

    private final UserService userService;
    private final OrderPaymentRepository orderPaymentRepository;

    private void validateOrderPayment(OrderPayment orderPayment) {
        if (orderPayment.getIdUser() == null) {
            throw new InvalidException("Không tìm thấy idUser");
        }
        if (orderPayment.getIdAddress() == null) {
            throw new InvalidException("Không tìm thấy idAddress");
        }
        if (orderPayment.getIdShoppingCart() == null) {
            throw new InvalidException("Không tìm thấy idShoppingCart");
        }
        if (orderPayment.getEstimatedDelivery() == null) {
            throw new InvalidException("Không tìm thấy estimatedDelivery");
        }
    }

    @Override
    public OrderPayment createOrderPayment(OrderPayment orderPayment) {
        validateOrderPayment(orderPayment);
        userService.checkExistUser(orderPayment.getIdUser());
        orderPayment.setOrderPaymentStatus(OrderPaymentStatus.ORDER_SUCCESSFUL);
        orderPayment.setCreatedAt(new Date());
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
        if (optionalOrderPayment.isEmpty()) throw new InvalidException("Không tìm thấy OrderPayment");
        return optionalOrderPayment.get();
    }

    @Override
    public List<OrderPayment> getAllOrderPaymentByIdUserAndOrderPaymentStatus
            (Long idUser, OrderPaymentStatus orderPaymentStatus) {
        return orderPaymentRepository.getAllOrderPaymentByIdUserAndOrderPaymentStatus(idUser, orderPaymentStatus);
    }
}
