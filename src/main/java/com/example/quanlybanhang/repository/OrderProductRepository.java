package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> getAllByUser(User user);

    Optional<OrderProduct> findAllByUserAndStatus(User user, String status);
}
