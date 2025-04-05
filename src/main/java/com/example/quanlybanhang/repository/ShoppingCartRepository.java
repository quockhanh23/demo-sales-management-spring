package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.ShoppingCart;
import com.example.quanlybanhang.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    List<ShoppingCart> getAllByIdUserAndStatus(Long idUser, String status);

    Optional<ShoppingCart> findAllByIdUserAndStatus(Long idUser, String status);
}
