package com.example.quanlybanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private Date dateOfBirth;
    private String phone;
    private String status;
    private String role;
    private boolean isBuyer;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
}
