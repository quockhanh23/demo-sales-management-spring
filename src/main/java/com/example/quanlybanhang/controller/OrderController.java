package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.dto.OrderProductDTO;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all-in-cart")
    public ResponseEntity<?> getAllProductInCart(@RequestParam Long idUser) {
        OrderProductDTO getAllProductInCart = orderProductService.getAllProductInCart(idUser);
        return new ResponseEntity<>(getAllProductInCart, HttpStatus.OK);
    }

    @GetMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestParam String status,
                                          @RequestParam Long idOrderProduct,
                                          @RequestParam Long idUser) {
        OrderProduct orderProduct = orderProductService.checkExistOrderProduct(idOrderProduct);
        if (!orderProduct.getUser().getId().equals(idUser)) {
            throw new BadRequestException(MessageConstants.ORDER_IS_NOT_OF_USE);
        }
        if (SalesManagementConstants.STATUS_ORDER_BOUGHT.equals(orderProduct.getStatus())) {
            throw new BadRequestException(MessageConstants.ORDER_HAS_BEEN_COMPLETED);
        }
        orderProduct.setStatus(status);
        orderProduct.setEditAt(new Date());
        orderProductService.save(orderProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/count-order")
    public ResponseEntity<?> countOrder(@RequestParam Long idUser) {
        return new ResponseEntity<>(orderProductService.countAllByUser(idUser), HttpStatus.OK);
    }
}
