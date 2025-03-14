package com.example.quanlybanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartDTO {
    private Long idOrderProduct;
    private Date createAt;
    private String status;
    private List<ShoppingCartDetailDTO> shoppingCartDetailDTOList;
    private BigDecimal totalPrice;
}
