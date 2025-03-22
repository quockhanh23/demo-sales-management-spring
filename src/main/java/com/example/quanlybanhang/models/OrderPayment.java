package com.example.quanlybanhang.models;

import com.example.quanlybanhang.common.OrderPaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long idUser;
    @NotNull
    private Long idShoppingCart;
    @NotNull
    private Long idAddress;
    @Column(length = 500)
    private String note;
    @NotNull
    private String totalOrderAmount;
    private String deliveryMethod;
    private String estimatedDelivery;
    private Date createdAt;
    private Date updatedAt;
    private OrderPaymentStatus orderPaymentStatus;

}
