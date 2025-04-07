package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.models.OrderPayment;
import com.example.quanlybanhang.models.OrderPaymentHistory;
import com.example.quanlybanhang.repository.OrderPaymentHistoryRepository;
import com.example.quanlybanhang.service.OrderPaymentService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class OrderPaymentController {

    private final UserService userService;
    private final OrderPaymentService orderPaymentService;
    private final OrderPaymentHistoryRepository orderPaymentHistoryRepository;

    @PostMapping("/create-payment")
    public ResponseEntity<Object> createPayment(@RequestBody OrderPayment orderPayment) {
        OrderPayment orderPaymentResponse = orderPaymentService.createOrderPayment(orderPayment);
        return new ResponseEntity<>(orderPaymentResponse, HttpStatus.OK);
    }

    @PutMapping("/update-status-payment")
    public ResponseEntity<Object> updateStatusPayment(@RequestParam Long idOrderPayment,
                                                      @RequestParam OrderPaymentStatus status) {
        OrderPayment orderPaymentResponse = orderPaymentService.updateStatusOrderPayment(idOrderPayment, status);
        return new ResponseEntity<>(orderPaymentResponse, HttpStatus.OK);
    }

    @GetMapping("/get-all-order-payment")
    public ResponseEntity<Object> getAllOrderPaymentByIdUserAndOrderPaymentStatus
            (@RequestParam Long idUser, @RequestParam(required = false) OrderPaymentStatus orderPaymentStatus) {
        List<OrderPayment> orderPaymentList = orderPaymentService
                .getAllOrderPaymentByIdUserAndOrderPaymentStatus(idUser, orderPaymentStatus);
        return new ResponseEntity<>(orderPaymentList, HttpStatus.OK);
    }

    @GetMapping("/get-all-payment")
    public ResponseEntity<Object> getAllOrderPaymentByOrderPaymentStatus
            (@RequestParam(required = false) OrderPaymentStatus orderPaymentStatus) {
        List<OrderPayment> orderPaymentList = orderPaymentService
                .getAllOrderPaymentByOrderPaymentStatus(orderPaymentStatus);
        return new ResponseEntity<>(orderPaymentList, HttpStatus.OK);
    }

    @GetMapping("/get-detail-order-payment")
    public ResponseEntity<Object> getDetailOrderPayment(@RequestParam Long idUser, @RequestParam Long idOrderPayment) {
        userService.checkExistUser(idUser);
        OrderPayment orderPayment = orderPaymentService.getDetailOrderPayment(idOrderPayment);
        return new ResponseEntity<>(orderPayment, HttpStatus.OK);
    }

    @GetMapping("/get-all-history-order-payment")
    public ResponseEntity<Object> getAllHistoryOfOrderPayment
            (@RequestParam Long idUser, @RequestParam Long idOrderPayment) {
        userService.checkExistUser(idUser);
        List<OrderPaymentHistory> orderPaymentHistories = orderPaymentHistoryRepository
                .getAllByIdOrderPayment(idOrderPayment);
        return new ResponseEntity<>(orderPaymentHistories, HttpStatus.OK);
    }
}
