package com.example.quanlybanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductDTO {
    private Long idOrderProduct;
    private Date createAt;
    private String status;
    private List<OrderProductDetailDTO> orderProductDetailDTOList;
}