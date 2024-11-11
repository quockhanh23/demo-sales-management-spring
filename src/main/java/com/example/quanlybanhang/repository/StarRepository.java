package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {

    List<Star> findAllByProductIdAndNumberOfStars(Long idProduct, String type);

    List<Star> findAllByProductId(Long idProduct);

    List<Star> findAllByIdUserAndProductId(Long idUser, Long idProduct);
}
