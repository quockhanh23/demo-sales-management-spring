package com.example.quanlybanhang.models;

import com.example.quanlybanhang.common.ShoppingCartDetailStatus;
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
public class ShoppingCartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idOrderProduct;
    private Long idProduct;
    @Column(length = 1)
    private int quantity;
    private Date createdAt;
    private Date updatedAt;
    private String price;
    private ShoppingCartDetailStatus status;
}
