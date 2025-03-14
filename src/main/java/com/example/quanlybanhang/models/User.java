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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 50)
    private String username;
    @Column(length = 40)
    private String password;
    @Column(length = 40)
    private String confirmPassword;
    private Date dateOfBirth;
    @Column(length = 20)
    private String phone;
    @Column(length = 20)
    private String pin;
    private String status;
    private String role;
    private boolean isBuyer;
    @Lob
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
}
