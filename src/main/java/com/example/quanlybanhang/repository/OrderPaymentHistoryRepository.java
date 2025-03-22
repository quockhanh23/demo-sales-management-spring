package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.OrderPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPaymentHistoryRepository extends JpaRepository<OrderPaymentHistory, Long> {

    List<OrderPaymentHistory> getAllByIdOrderPayment(Long idOrder);
}
