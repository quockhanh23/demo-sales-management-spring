package com.example.quanlybanhang.service;

import com.example.quanlybanhang.models.ProductRating;

import java.util.List;
import java.util.Optional;

public interface ProductRatingService {

    Optional<ProductRating> findById(Long id);

    void save(ProductRating productRating);

    void delete(Long id);

    List<ProductRating> findAllByProductIdAndType(Long idProduct, String type);

    List<ProductRating> findAllByProductId(Long idProduct);

    ProductRating initStar(Long idUser, Long idProduct, String type);

    ProductRating findStarByUserAndProduct(Long idUser, Long idProduct);
}
