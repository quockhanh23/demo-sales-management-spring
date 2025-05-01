package com.example.quanlybanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String productName;
    private String description;
    private String price;
    private int quantity;
    private byte[] image;
    private boolean outOfStock;
}
