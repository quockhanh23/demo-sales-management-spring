package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody Product product, @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        productService.validateProduct(product);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/update-status-product")
    public ResponseEntity<?> updateStatusProduct(@RequestParam String status,
                                                 @RequestParam Long idProduct,
                                                 @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        Optional<Product> productOptional = productService.findById(idProduct);
        if (productOptional.isEmpty()) {
            throw new BadRequestException(MessageConstants.NOT_FOUND_PRODUCT);
        }
        productOptional.get().setStatus(status);
        productService.save(productOptional.get());
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    @PutMapping("/update-product")
    public ResponseEntity<?> updateInformationProduct(@RequestBody Product product,
                                                      @RequestParam Long idProduct,
                                                      @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        Optional<Product> productOptional = productService.findById(idProduct);
        if (productOptional.isEmpty()) {
            throw new BadRequestException(MessageConstants.NOT_FOUND_PRODUCT);
        }
        productOptional.get().setStatus(product.getStatus());
        productOptional.get().setProductName(product.getProductName());
        productOptional.get().setQuantity(product.getQuantity());
        productOptional.get().setPrice(product.getPrice());
        productService.save(productOptional.get());
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestParam Long idProduct, @RequestParam Long idUser) {
        try {
            userService.checkRoleAdmin(idUser);
            Optional<Product> productOptional = productService.findById(idProduct);
            if (productOptional.isEmpty()) {
                throw new BadRequestException(MessageConstants.NOT_FOUND_PRODUCT);
            }
            productOptional.get().setDelete(true);
            productService.save(productOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProduct() throws IOException {
        List<ProductDTO> productList = productService.getAllProduct();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/detailProduct")
    public ResponseEntity<?> getDetailProduct(@RequestParam Long idProduct) throws IOException {
        Product product = productService.checkExistUser(idProduct);
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        productDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
