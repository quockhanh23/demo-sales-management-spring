package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.OrderProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductDetailRepository extends JpaRepository<OrderProductDetail, Long> {

    List<OrderProductDetail> findAllByIdOrderProduct(Long idOrderProduct);
}
