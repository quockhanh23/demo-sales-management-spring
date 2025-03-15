package com.example.quanlybanhang.service;

import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {

    Optional<Product> findById(Long id);

    void save(Product product);

    void saveAll(Iterable<Product> product);

    void delete(Long id);

    Set<Product> findAllByIdProductIn(List<Long> id);

    void validateProduct(Product product);

    Product checkExistProduct(Long idProduct);

    Page<ProductDTO> getAllProductPage(Pageable pageable) throws IOException;
}
