package com.example.quanlybanhang.service;

import com.example.quanlybanhang.dto.OrderProductDTO;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.models.OrderProduct;

import java.io.IOException;
import java.util.Optional;

public interface OrderProductService {

    Optional<OrderProduct> findById(Long id);

    void save(OrderProduct orderProduct);

    void delete(Long id);

    OrderProduct checkExistOrderProduct(Long idOrder);

    long countAllByUser(Long idUser);

    void addToCart(Long idUser, ProductDTO productDTO);

    OrderProductDTO getAllProductInCart(Long idUser) throws IOException;
}
