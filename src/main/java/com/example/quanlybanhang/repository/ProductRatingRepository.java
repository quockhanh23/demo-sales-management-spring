package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

    List<ProductRating> findAllByProductIdAndNumberOfRate(Long idProduct, String type);

    List<ProductRating> findAllByProductId(Long idProduct);

    List<ProductRating> findAllByIdUserAndProductId(Long idUser, Long idProduct);
}
