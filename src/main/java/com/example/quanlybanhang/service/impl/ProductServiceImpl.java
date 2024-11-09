package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.repository.ProductRepository;
import com.example.quanlybanhang.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Set<Product> findAllByIdProductIn(List<Long> id) {
        return productRepository.findAllByIdProductIn(id);
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> productList = productRepository.getAllProduct();
        if (CollectionUtils.isEmpty(productList)) return new ArrayList<>();
        return productList;
    }

    @Override
    public void validateProduct(Product product) {
        if (StringUtils.isEmpty(product.getProductName())) {
            throw new BadRequestException("Tên sản phẩm không được để trống");
        }
        if (StringUtils.isEmpty(product.getPrice())) {
            throw new BadRequestException("Giá sản phẩm không được để trống");
        }
        if (product.getQuantity() <= 0) {
            throw new BadRequestException("Số lượng sản phẩm phải lớn hơn 0");
        }
    }
}
