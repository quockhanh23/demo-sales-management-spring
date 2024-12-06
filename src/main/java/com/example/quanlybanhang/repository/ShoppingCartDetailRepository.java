package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.ShoppingCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartDetailRepository extends JpaRepository<ShoppingCartDetail, Long> {

    List<ShoppingCartDetail> findAllByIdOrderProduct(Long idOrderProduct);
}
