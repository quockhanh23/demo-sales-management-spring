package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
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
        try {
            userService.checkRoleAdmin(idUser);
            productService.validateProduct(product);
            product.setStatus("Hoạt động");
            productService.save(product);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/update-status-product")
    public ResponseEntity<?> updateStatusProduct(@RequestParam String status,
                                                 @RequestParam Long idProduct,
                                                 @RequestParam Long idUser) {
        try {
            userService.checkRoleAdmin(idUser);
            Optional<Product> productOptional = productService.findById(idProduct);
            if (productOptional.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sản phẩm", HttpStatus.BAD_REQUEST);
            }
            productOptional.get().setStatus(status);
            productService.save(productOptional.get());
            return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-product")
    public ResponseEntity<?> updateInformationProduct(@RequestBody Product product,
                                                      @RequestParam Long idProduct,
                                                      @RequestParam Long idUser) {
        try {
            userService.checkRoleAdmin(idUser);
            Optional<Product> productOptional = productService.findById(idProduct);
            if (productOptional.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sản phẩm", HttpStatus.BAD_REQUEST);
            }
            productOptional.get().setStatus(product.getStatus());
            productOptional.get().setProductName(product.getProductName());
            productOptional.get().setQuantity(product.getQuantity());
            productOptional.get().setPrice(product.getPrice());
            productService.save(productOptional.get());
            return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestParam Long idProduct, @RequestParam Long idUser) {
        try {
            userService.checkRoleAdmin(idUser);
            Optional<Product> productOptional = productService.findById(idProduct);
            if (productOptional.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sản phẩm", HttpStatus.BAD_REQUEST);
            }
            productOptional.get().setDelete(true);
            productService.save(productOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProduct() {
        try {
            List<Product> productList = productService.getAllProduct();
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (Product product : productList) {
                ProductDTO productDTO = new ProductDTO();
                byte[] imageBytes = Files.readAllBytes(Paths.get(product.getImage()));
                productDTO.setImage(imageBytes);
                productDTO.setName(product.getProductName());
                productDTOS.add(productDTO);
            }
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
