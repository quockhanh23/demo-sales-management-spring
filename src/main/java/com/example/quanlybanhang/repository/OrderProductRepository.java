package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    long countAllByUser(User user);
}
