package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Set<Product> findAllByIdProductIn(List<Long> id);

    @Query(value = "select * from product where (product_name LIKE CONCAT('%', :productName, '%')  OR :productName IS NULL) and is_delete = false and status = 'ACTIVE' and quantity > 0", nativeQuery = true)
    Page<Product> getAllSearchProductPage(String productName, Pageable pageable);

    @Query(value = "select * from product where is_delete = false and status = 'ACTIVE' and quantity > 0", nativeQuery = true)
    Page<Product> getAllProductPage(Pageable pageable);
}
