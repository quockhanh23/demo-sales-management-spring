package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.constant.CommonConstant;
import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.constant.UploadFileConstant;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.repository.ProductRepository;
import com.example.quanlybanhang.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

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
    public void saveAll(Iterable<Product> product) {
        productRepository.saveAll(product);
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
    public void validateProduct(Product product) {
        if (StringUtils.isEmpty(product.getProductName())) {
            throw new InvalidException(MessageConstants.PRODUCT_NAME_NOT_EMPTY);
        }
        if (StringUtils.isEmpty(product.getPrice())) {
            throw new InvalidException(MessageConstants.PRODUCT_PRICE_NOT_EMPTY);
        }
        if (product.getQuantity() <= 0) {
            throw new InvalidException(MessageConstants.PRODUCT_QUANTITY_MUST_GREAT_THAN_ZERO);
        }
        if (StringUtils.isEmpty(product.getImage())) {
            product.setImage(UploadFileConstant.SRC_IMAGE_PROJECT + SalesManagementConstants.DEFAULT_NO_IMAGE);
        }
        if (product.getDescription().length() > 500) {
            throw new InvalidException(MessageConstants.PRODUCT_DESCRIPTION_MAX_SIZE);
        }
        if (StringUtils.isEmpty(product.getImage())) {
            throw new InvalidException(MessageConstants.IMAGE_NOT_EMPTY);
        }
        product.setStatus(SalesManagementConstants.STATUS_ACTIVE);
        product.setCreatedAt(new Date());
    }

    public Product checkExistProduct(Long idProduct) {
        Optional<Product> product = findById(idProduct);
        if (product.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_PRODUCT);
        } else {
            return product.get();
        }
    }

    @Override
    public void updateProduct(Product productRequest, Long idProduct) {
        Product product = checkExistProduct(idProduct);
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());
        product.setUpdatedAt(new Date());
        productRepository.save(product);
    }

    @Override
    public Page<ProductDTO> getAllProductPage(String productName, String stock, Pageable pageable) throws IOException {
        Page<Product> page;
        if (StringUtils.isEmpty(productName)) {
            page = CommonConstant.IN_STOCK.equals(stock)
                    ? productRepository.getAllProductPage(pageable)
                    : productRepository.getAllProductPageOutStock(pageable);
        } else {
            page = CommonConstant.IN_STOCK.equals(stock)
                    ? productRepository.getAllSearchProductPage(productName, pageable)
                    : productRepository.getAllSearchProductPageOutStock(productName, pageable);
        }
        List<Product> productList = page.getContent();
        List<ProductDTO> productDTOList = convertToProductDTO(productList);
        return new PageImpl<>(productDTOList);
    }

    private List<ProductDTO> convertToProductDTO(List<Product> productList) throws IOException {
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productList)) {
            for (Product product : productList) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
                productDTO.setId(product.getIdProduct());
                productDTO.setProductName(product.getProductName());
                productDTO.setPrice(product.getPrice());
                productDTO.setQuantity(product.getQuantity());
                productDTO.setOutOfStock(product.isOutOfStock());
                productDTOS.add(productDTO);
            }
        }
        return productDTOS;
    }
}
