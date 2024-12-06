package com.example.quanlybanhang.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    private Date createdAt;
    private Date updatedAt;
    private String status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_shopping_cart",
            joinColumns = {@JoinColumn(name = "shopping_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private List<ShoppingCartDetail> productDetails;
}
