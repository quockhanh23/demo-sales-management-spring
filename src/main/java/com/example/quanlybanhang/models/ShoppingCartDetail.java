package com.example.quanlybanhang.models;

import com.example.quanlybanhang.common.ShoppingCartDetailStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private int quantity;
    private Date createdAt;
    private Date updatedAt;
    private String price;
    private ShoppingCartDetailStatus status;
}
