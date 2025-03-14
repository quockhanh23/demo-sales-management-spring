package com.example.quanlybanhang.models;

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
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String street;
    private String note;
    private String deliveryTime;
    private Date createdAt;
    private Date updatedAt;
    @Column(length = 20)
    private String status;
}
