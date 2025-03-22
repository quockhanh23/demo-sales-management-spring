package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.models.OrderPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPaymentHistoryRepository extends JpaRepository<OrderPaymentHistory, Long> {

    List<OrderPaymentHistory> getAllByIdOrderPaymentAndStatus(Long idOrder, OrderPaymentStatus orderPaymentStatus);
}
