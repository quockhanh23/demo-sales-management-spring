package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.dto.OrderProductDTO;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProductService orderProductService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestParam Long idUser,
                                       @RequestBody ProductDTO productDTO) {
        try {
            orderProductService.addToCart(idUser, productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-from-cart")
    public ResponseEntity<?> removeFromCart(@RequestParam Long idUser,
                                            @RequestParam Long idProduct) {
        try {
            orderProductService.removeToCart(idUser, idProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/decrease-product")
    public ResponseEntity<?> decreaseProduct(@RequestParam Long idUser,
                                             @RequestParam Long idProduct) {
        try {
            orderProductService.decreaseProduct(idUser, idProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/increase-product")
    public ResponseEntity<?> increaseProduct(@RequestParam Long idUser,
                                             @RequestParam Long idProduct) {
        try {
            orderProductService.increaseProduct(idUser, idProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all-in-cart")
    public ResponseEntity<?> getAllProductInCart(@RequestParam Long idUser) {
        OrderProductDTO getAllProductInCart;
        try {
            getAllProductInCart = orderProductService.getAllProductInCart(idUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(getAllProductInCart, HttpStatus.OK);
    }

    @GetMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestParam String status,
                                          @RequestParam Long idOrderProduct,
                                          @RequestParam Long idUser) {
        try {
            orderProductService.changeStatus(idOrderProduct, idUser, status);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/count-order")
    public ResponseEntity<?> countOrder(@RequestParam Long idUser) {
        long count;
        try {
            count = orderProductService.countAllByUser(idUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
