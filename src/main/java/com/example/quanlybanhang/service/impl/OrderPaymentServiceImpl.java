package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.OrderPayment;
import com.example.quanlybanhang.models.OrderPaymentHistory;
import com.example.quanlybanhang.repository.OrderPaymentHistoryRepository;
import com.example.quanlybanhang.repository.OrderPaymentRepository;
import com.example.quanlybanhang.service.OrderPaymentService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {

    private final UserService userService;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderPaymentHistoryRepository orderPaymentHistoryRepository;

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
        OrderPayment orderPayment1 = orderPaymentRepository.save(orderPayment);
        OrderPaymentHistory orderPaymentHistory =
                initOrderPaymentHistory(orderPayment1.getOrderPaymentStatus(), orderPayment1.getId());
        orderPaymentHistoryRepository.save(orderPaymentHistory);
        return orderPayment1;
    }

    private OrderPaymentHistory initOrderPaymentHistory(OrderPaymentStatus orderPaymentStatus, Long idOrderPayment) {
        OrderPaymentHistory orderPaymentHistory = new OrderPaymentHistory();
        orderPaymentHistory.setIdOrderPayment(idOrderPayment);
        orderPaymentHistory.setStatus(orderPaymentStatus);
        orderPaymentHistory.setCreatedAt(new Date());
        return orderPaymentHistory;
    }

    @Override
    public OrderPayment updateStatusOrderPayment(Long idOrderPayment, OrderPaymentStatus status) {
        OrderPayment orderPayment = getDetailOrderPayment(idOrderPayment);
        orderPayment.setOrderPaymentStatus(status);
        OrderPaymentHistory orderPaymentHistory =
                initOrderPaymentHistory(status, orderPayment.getId());
        orderPaymentHistoryRepository.save(orderPaymentHistory);
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
        List<OrderPayment> orderPaymentList;
        if (Objects.isNull(orderPaymentStatus)) {
            orderPaymentList = orderPaymentRepository.getAllOrderPaymentByIdUser(idUser);
        } else {
            orderPaymentList = orderPaymentRepository.
                    getAllOrderPaymentByIdUserAndOrderPaymentStatus(idUser, orderPaymentStatus);
        }
        if (CollectionUtils.isEmpty(orderPaymentList)) return new ArrayList<>();
        return orderPaymentList;
    }
}
