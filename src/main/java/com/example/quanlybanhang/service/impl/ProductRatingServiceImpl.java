package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.ProductRating;
import com.example.quanlybanhang.repository.ProductRatingRepository;
import com.example.quanlybanhang.service.ProductRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductRatingServiceImpl implements ProductRatingService {

    private final ProductRatingRepository productRatingRepository;

    @Override
    public Optional<ProductRating> findById(Long id) {
        return productRatingRepository.findById(id);
    }

    @Override
    public void save(ProductRating productRating) {
        productRatingRepository.save(productRating);
    }

    @Override
    public void delete(Long id) {
        productRatingRepository.deleteById(id);
    }

    @Override
    public List<ProductRating> findAllByProductIdAndType(Long idProduct, String type) {
        List<ProductRating> productRatingList = productRatingRepository.findAllByProductIdAndNumberOfRate(idProduct, type);
        if (CollectionUtils.isEmpty(productRatingList)) productRatingList = new ArrayList<>();
        return productRatingList;
    }

    @Override
    public List<ProductRating> findAllByProductId(Long idProduct) {
        List<ProductRating> productRatingList = productRatingRepository.findAllByProductId(idProduct);
        if (CollectionUtils.isEmpty(productRatingList)) productRatingList = new ArrayList<>();
        return productRatingList;
    }

    @Override
    public ProductRating initStar(Long idUser, Long idProduct, String type) {
        String[] rangeOfType = {"1", "2", "3", "4", "5"};
        int count = 0;
        for (String s : rangeOfType) {
            if (type.equals(s)) {
                break;
            } else {
                count++;
            }
        }
        if (count == 5) {
            throw new InvalidException("Chỉ có thể đánh giá từ 1 đến 5 sao");
        }
        ProductRating productRating = new ProductRating();
        productRating.setNumberOfRate(type);
        productRating.setProductId(idProduct);
        productRating.setIdUser(idUser);
        return productRating;
    }

    @Override
    public ProductRating findStarByUserAndProduct(Long idUser, Long idProduct) {
        List<ProductRating> list = productRatingRepository.findAllByIdUserAndProductId(idUser, idProduct);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
