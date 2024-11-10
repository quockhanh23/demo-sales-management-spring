package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.service.OrderProductService;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final UserService userService;
    private final OrderProductService orderProductService;
    private final ProductService productService;

    @GetMapping("/add-to-cart")
    public ResponseEntity<?> createOrder(@RequestParam(required = false) Long idOrder,
                                         @RequestParam Long idUser,
                                         @RequestParam List<Long> listIdProduct) {
        User user = userService.checkExistUser(idUser);
        Set<Product> productSet = productService.findAllByIdProductIn(listIdProduct);
        Long id = 0L;
        if (null != idOrder) {
            id = idOrder;
        }
        Optional<OrderProduct> optionalOrderProduct = orderProductService.findById(id);
        if (optionalOrderProduct.isEmpty()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setCreateAt(new Date());
            orderProduct.setUser(user);
            orderProduct.setProductSet(productSet);
            orderProduct.setStatus(SalesManagementConstants.STATUS_ORDER_PENDING);
            orderProductService.save(orderProduct);
            return new ResponseEntity<>(orderProduct, HttpStatus.OK);
        }
        optionalOrderProduct.get().setEditAt(new Date());
        optionalOrderProduct.get().setProductSet(productSet);
        orderProductService.save(optionalOrderProduct.get());
        return new ResponseEntity<>(optionalOrderProduct.get(), HttpStatus.OK);
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
        User user = userService.checkExistUser(idUser);
        return new ResponseEntity<>(orderProductService.countAllByUser(user), HttpStatus.OK);
    }
}
