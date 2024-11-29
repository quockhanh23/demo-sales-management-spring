package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.dto.ProductDTO;
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

import java.io.IOException;

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
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/update-status-product")
    public ResponseEntity<?> updateStatusProduct(@RequestParam String status,
                                                 @RequestParam Long idProduct,
                                                 @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        Product product = productService.checkExistProduct(idProduct);
        product.setStatus(status);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/update-product")
    public ResponseEntity<?> updateInformationProduct(@RequestBody Product product,
                                                      @RequestParam Long idProduct,
                                                      @RequestParam Long idUser) {
        userService.checkRoleAdmin(idUser);
        Product productUpdate = productService.checkExistProduct(idProduct);
        productUpdate.setStatus(product.getStatus());
        productUpdate.setProductName(product.getProductName());
        productUpdate.setQuantity(product.getQuantity());
        productUpdate.setPrice(product.getPrice());
        productService.save(productUpdate);
        return new ResponseEntity<>(productUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestParam Long idProduct, @RequestParam Long idUser) {
        try {
            userService.checkRoleAdmin(idUser);
            Product product = productService.checkExistProduct(idProduct);
            product.setDelete(true);
            productService.save(product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<Page<ProductDTO>> getAllProductPage
            (@RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(defaultValue = "10", required = false) int size) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productDTOPage = productService.getAllProductPage(pageable);
        return new ResponseEntity<>(productDTOPage, HttpStatus.OK);
    }

    @GetMapping("/detailProduct")
    public ResponseEntity<?> getDetailProduct(@RequestParam Long idProduct) throws IOException {
        Product product = productService.checkExistProduct(idProduct);
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        productDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
