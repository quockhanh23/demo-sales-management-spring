package com.example.quanlybanhang.models;

import com.example.quanlybanhang.common.AddressStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    @Column(length = 300)
    private String address;
    private String province;
    private String district;
    private String ward;
    @NotNull
    private Long idUser;
    private Boolean inUse;
    @Column(length = 10)
    private AddressStatus status;
}
