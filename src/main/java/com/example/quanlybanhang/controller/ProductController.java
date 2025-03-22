package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/create-product")
    public ResponseEntity<Object> createProduct(@RequestBody Product product, @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        productService.validateProduct(product);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/update-status-product")
    public ResponseEntity<Object> updateStatusProduct(@RequestParam String status,
                                                      @RequestParam Long idProduct,
                                                      @RequestParam Long idUser) {

        userService.checkRoleAdmin(idUser);
        Product product = productService.checkExistProduct(idProduct);
        product.setStatus(status);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/update-product")
    public ResponseEntity<Object> updateInformationProduct(@RequestBody Product product,
                                                           @RequestParam Long idProduct,
                                                           @RequestParam Long idUser) {

        userService.checkRoleAdmin(idUser);
        Product productUpdate = productService.checkExistProduct(idProduct);
        productUpdate.setStatus(product.getStatus());
        productUpdate.setProductName(product.getProductName());
        productUpdate.setQuantity(product.getQuantity());
        productUpdate.setPrice(product.getPrice());
        productUpdate.setUpdatedAt(new Date());
        productService.save(productUpdate);
        return new ResponseEntity<>(productUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<Object> deleteProduct(@RequestParam Long idProduct, @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        Product product = productService.checkExistProduct(idProduct);
        product.setDelete(true);
        productService.save(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<Object> getAllProductPage(@RequestParam(required = false) String productName,
                                                    @RequestParam(defaultValue = "0", required = false) int page,
                                                    @RequestParam(defaultValue = "10", required = false) int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> productDTOPage = productService.getAllProductPage(productName, pageable);
            return new ResponseEntity<>(productDTOPage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detailProduct")
    public ResponseEntity<Object> getDetailProduct(@RequestParam Long idProduct) {
        try {
            ProductDTO productDTO = new ProductDTO();
            Product product = productService.checkExistProduct(idProduct);
            BeanUtils.copyProperties(product, productDTO);
            productDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
