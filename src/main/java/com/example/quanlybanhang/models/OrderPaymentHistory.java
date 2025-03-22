package com.example.quanlybanhang.models;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderPaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idOrderPayment;
    @Column(length = 20)
    private OrderPaymentStatus status;
    private Date createdAt;
}
