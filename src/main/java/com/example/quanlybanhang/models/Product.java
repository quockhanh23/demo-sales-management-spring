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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;
    @Column(length = 200)
    private String productName;
    @Column(length = 150)
    private String productType;
    private String price;
    private int quantity;
    @Column(length = 20)
    private String status;
    private boolean isDelete;
    @Lob
    private String image;
    @Column(length = 500)
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
