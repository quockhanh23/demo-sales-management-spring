package com.example.quanlybanhang.service;

import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.models.User;

import java.util.List;
import java.util.Optional;

public interface OrderProductService {

    Optional<OrderProduct> findById(Long id);

    void save(OrderProduct orderProduct);

    void delete(Long id);

    OrderProduct checkExistOrderProduct(Long idOrder);

    long countAllByUser(User user);
}
