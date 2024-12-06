package com.example.quanlybanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartDetailDTO {
    private Long id;
    private Long idOrderProduct;
    private Long idProduct;
    private int quantity;
    private int totalQuantity;
    private String productName;
    private String price;
    private String totalPrice;
    private byte[] image;
}
