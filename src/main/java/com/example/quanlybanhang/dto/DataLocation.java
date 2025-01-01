package com.example.quanlybanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataLocation {
    private String id;
    private String name;
    private String provinceId;
    private String districtId;
    private int type;
    private String typeText;
    private String slug;
}
