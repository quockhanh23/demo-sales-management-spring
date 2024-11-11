package com.example.quanlybanhang.service;

import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.models.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {

    Optional<Product> findById(Long id);

    void save(Product product);

    void delete(Long id);

    Set<Product> findAllByIdProductIn(List<Long> id);

    List<ProductDTO> getAllProduct() throws IOException;

    void validateProduct(Product product);

    Product checkExistUser(Long idProduct);
}
