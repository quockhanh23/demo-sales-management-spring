package com.example.quanlybanhang.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    HOUSEHOLD("Đồ dân dụng", "01"),
    ELECTRONICS("Đồ điện tử", "02"),
    FOOD("Thực phẩm", "03");
    private final String name;
    private final String code;
}
