package com.example.quanlybanhang.service;

import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.dto.ShoppingCartDTO;
import com.example.quanlybanhang.dto.ShoppingCartDetailDTO;
import com.example.quanlybanhang.models.ShoppingCart;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {

    Optional<ShoppingCart> findById(Long id);

    ShoppingCart checkExistOrderProduct(Long idOrder);

    long countAllByUser(Long idUser);

    void addToCart(Long idUser, ProductDTO productDTO);

    void removeToCart(Long idUser, Long idProduct);

    void decreaseProduct(Long idUser, Long idProduct);

    void increaseProduct(Long idUser, Long idProduct);

    ShoppingCartDTO getAllProductInCart(Long idUser) throws IOException;

    void changeStatus(Long idOrderProduct, Long idUser, String status);

    List<ShoppingCartDTO> getAllComplete(Long idUser);
}
