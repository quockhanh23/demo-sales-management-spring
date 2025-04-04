package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.models.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {

    List<OrderPayment> getAllOrderPaymentByIdUserAndOrderPaymentStatus
            (Long idUser, OrderPaymentStatus orderPaymentStatus);

    List<OrderPayment> getAllOrderPaymentByIdUser(Long idUser);

    List<OrderPayment> getAllOrderPaymentByOrderPaymentStatus(OrderPaymentStatus status);
}
