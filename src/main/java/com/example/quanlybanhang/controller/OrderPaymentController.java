package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.OrderPayment;
import com.example.quanlybanhang.service.OrderPaymentService;
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

    private final OrderPaymentService orderPaymentService;

    @GetMapping("/get-all-payment")
    public ResponseEntity<Object> getAllPaymentComplete(@RequestParam Long idUser,
                                                        @RequestParam OrderPaymentStatus orderPaymentStatus) {
        try {
            List<OrderPayment> orderPaymentList = orderPaymentService
                    .getAllOrderPaymentByIdUserAndOrderPaymentStatus(idUser, orderPaymentStatus);
            return new ResponseEntity<>(orderPaymentList, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-payment")
    public ResponseEntity<Object> createPayment(@RequestBody OrderPayment orderPayment) {
        try {
            OrderPayment orderPaymentResponse = orderPaymentService.createOrderPayment(orderPayment);
            return new ResponseEntity<>(orderPaymentResponse, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-status-payment")
    public ResponseEntity<Object> updateStatusPayment(@RequestParam Long idOrderPayment, @RequestParam String status) {
        try {
            OrderPayment orderPaymentResponse = orderPaymentService.updateStatusOrderPayment(idOrderPayment, status);
            return new ResponseEntity<>(orderPaymentResponse, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-order-payment")
    public ResponseEntity<Object> getAllOrderPayment(@RequestParam Long idUser, @RequestParam String status) {
        try {

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
