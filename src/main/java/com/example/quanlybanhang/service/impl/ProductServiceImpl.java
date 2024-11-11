package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.repository.ProductRepository;
import com.example.quanlybanhang.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
    public List<ProductDTO> getAllProduct() throws IOException {
        List<Product> productList = productRepository.getAllProduct();
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(productList)) return productDTOS;
        for (Product product : productList) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
            productDTO.setId(product.getIdProduct());
            productDTO.setName(product.getProductName());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    @Override
    public void validateProduct(Product product) {
        if (StringUtils.isEmpty(product.getProductName())) {
            throw new BadRequestException(MessageConstants.PRODUCT_NAME_NOT_EMPTY);
        }
        if (StringUtils.isEmpty(product.getPrice())) {
            throw new BadRequestException(MessageConstants.PRODUCT_PRICE_NOT_EMPTY);
        }
        if (product.getQuantity() <= 0) {
            throw new BadRequestException(MessageConstants.PRODUCT_QUANTITY_MUST_GREAT_THAN_ZERO);
        }
        if (StringUtils.isEmpty(product.getImage())) {
            product.setImage(SalesManagementConstants.SRC_IMAGE + SalesManagementConstants.DEFAULT_NO_IMAGE);
        }
        product.setStatus(SalesManagementConstants.STATUS_ACTIVE);
    }

    public Product checkExistUser(Long idProduct) {
        Optional<Product> product = findById(idProduct);
        if (product.isEmpty()) {
            throw new BadRequestException(MessageConstants.NOT_FOUND_PRODUCT);
        } else {
            return product.get();
        }
    }
}
